package dev.pgm.cosmic_flow.components

import android.graphics.BlendMode
import android.graphics.Paint
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import dev.pgm.cosmic_flow.config.ParticleFieldDefaults
import dev.pgm.cosmic_flow.models.Particle
import dev.pgm.cosmic_flow.utils.hslToColor
import dev.pgm.cosmic_flow.utils.noiseAngle
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

// ===== Constantes base para evitar números literales =====
private const val ZERO_FLOAT = 0f
private const val ONE_FLOAT = 1f
private const val ZERO_LONG: Long = 0L

@Composable
internal fun ParticleField(
    modifier: Modifier,
    count: Int,
    touch: Offset?,
    tapTrigger: Int
) {
    val particles = remember(count) {
        List(count) { index ->
            Particle(
                offset = Offset.Zero,
                velocity = Offset.Zero,
                life = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_LIFE_RANGE +
                        ParticleFieldDefaults.PARTICLE_LIFE_MIN,
                hue = (index.toFloat() / count.toFloat()) * ParticleFieldDefaults.PARTICLE_HUE_DIVISOR_FACTOR,
                energy = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_ENERGY_RANGE +
                        ParticleFieldDefaults.PARTICLE_ENERGY_MIN
            )
        }
    }

    val startTimeNanos = remember { System.nanoTime() }

    val frameTimestampNanos by produceState(ZERO_LONG) {
        while (isActive) withFrameNanos { value = it }
    }

    LaunchedEffect(tapTrigger, touch) {
        touch ?: return@LaunchedEffect
        repeat(ParticleFieldDefaults.TAP_BURST_REPEAT_COUNT) {
            val randomIndex = Random.nextInt(particles.size)
            val randomAngle = Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_ANGLE_MULTIPLIER
            val initialSpeed =
                Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_SPEED_RANGE +
                        ParticleFieldDefaults.TAP_BURST_SPEED_MIN

            particles[randomIndex].offset = touch + Offset(
                Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_OFFSET_RANGE -
                        ParticleFieldDefaults.TAP_BURST_OFFSET_SHIFT,
                Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_OFFSET_RANGE -
                        ParticleFieldDefaults.TAP_BURST_OFFSET_SHIFT
            )
            particles[randomIndex].velocity = Offset(
                cos(randomAngle) * initialSpeed,
                sin(randomAngle) * initialSpeed
            )
            particles[randomIndex].life = ParticleFieldDefaults.TAP_BURST_LIFE_RESET
            particles[randomIndex].energy = ParticleFieldDefaults.TAP_BURST_ENERGY_RESET
        }
    }

    Canvas(modifier) {
        // Mantener dependencia a frame para forzar redraw sin usar una sola letra
        val _unusedFrameDependency = frameTimestampNanos

        val canvasWidth = size.width
        val canvasHeight = size.height

        fun resetParticle(index: Int) {
            particles[index].offset = Offset(
                Random.nextFloat() * canvasWidth,
                Random.nextFloat() * canvasHeight
            )
            particles[index].velocity = Offset.Zero
            particles[index].life =
                Random.nextFloat() * ParticleFieldDefaults.PARTICLE_LIFE_RANGE +
                        ParticleFieldDefaults.PARTICLE_LIFE_MIN
            particles[index].energy =
                Random.nextFloat() * ParticleFieldDefaults.PARTICLE_ENERGY_RANGE +
                        ParticleFieldDefaults.PARTICLE_ENERGY_MIN
        }

        particles.indices.forEach { particleIndex ->
            if (particles[particleIndex].offset == Offset.Zero) resetParticle(particleIndex)
        }

        val currentTimeNanos = System.nanoTime()
        val elapsedSeconds =
            (currentTimeNanos - startTimeNanos) / ParticleFieldDefaults.NANOS_TO_SECONDS
        val deltaSeconds = ParticleFieldDefaults.FRAME_TIME_SECONDS

        drawIntoCanvas { canvas ->
            val frameworkPaint = Paint().apply {
                isAntiAlias = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    blendMode = BlendMode.PLUS
                }
            }
            val nativeCanvas = canvas.nativeCanvas
            nativeCanvas.saveLayer(null, frameworkPaint)

            for (particleIndex in particles.indices) {
                val particle = particles[particleIndex]

                val angleFromNoise = noiseAngle(
                    particle.offset.x * ParticleFieldDefaults.FIELD_X_COORD_FACTOR,
                    particle.offset.y * ParticleFieldDefaults.FIELD_Y_COORD_FACTOR,
                    elapsedSeconds * ParticleFieldDefaults.FIELD_T_COORD_FACTOR
                )

                val swirlAngle =
                    ParticleFieldDefaults.FIELD_ANGLE_SWIRL_STRENGTH *
                            sin(elapsedSeconds * ParticleFieldDefaults.FIELD_ANGLE_T_FACTOR)

                val totalFieldAngle = angleFromNoise + swirlAngle

                val fieldForce = Offset(
                    cos(totalFieldAngle),
                    sin(totalFieldAngle)
                ) * ParticleFieldDefaults.FIELD_FORCE_MAGNITUDE_BASE * particle.energy

                val attractForce = touch?.let { touchPosition ->
                    val directionToTouch = touchPosition - particle.offset
                    val distanceToTouch =
                        max(ParticleFieldDefaults.ATTRACT_DISTANCE_GUARD_MIN, directionToTouch.getDistance())
                    val falloffFactor =
                        (ParticleFieldDefaults.ATTRACT_FALLOFF_BASE -
                                (distanceToTouch / ParticleFieldDefaults.ATTRACT_RADIUS_PX))
                            .coerceIn(ZERO_FLOAT, ONE_FLOAT)

                    if (falloffFactor <= ZERO_FLOAT) {
                        Offset.Zero
                    } else {
                        val rawAttract =
                            (directionToTouch / distanceToTouch) *
                                    (ParticleFieldDefaults.ATTRACT_FORCE_BASE_FACTOR / distanceToTouch) *
                                    falloffFactor * particle.energy

                        val rawMagnitude = rawAttract.getDistance()
                        if (rawMagnitude > ParticleFieldDefaults.MAX_ATTRACT_FORCE_MAGNITUDE) {
                            rawAttract * (ParticleFieldDefaults.MAX_ATTRACT_FORCE_MAGNITUDE / rawMagnitude)
                        } else {
                            rawAttract
                        }
                    }
                } ?: Offset.Zero

                val velocityDamping =
                    ParticleFieldDefaults.VELOCITY_DAMPING_BASE -
                            particle.energy * ParticleFieldDefaults.VELOCITY_DAMPING_ENERGY_FACTOR

                particle.velocity = (particle.velocity + (fieldForce + attractForce) * deltaSeconds) * velocityDamping

                val speedMagnitude = particle.velocity.getDistance()
                if (speedMagnitude > ParticleFieldDefaults.MAX_PARTICLE_SPEED_PX) {
                    particle.velocity = particle.velocity * (ParticleFieldDefaults.MAX_PARTICLE_SPEED_PX / speedMagnitude)
                }

                particle.offset += particle.velocity * deltaSeconds
                particle.life -= ParticleFieldDefaults.PARTICLE_LIFE_DECREMENT
                particle.energy = max(
                    ParticleFieldDefaults.PARTICLE_ENERGY_MIN_GUARD,
                    particle.energy - ParticleFieldDefaults.PARTICLE_ENERGY_DECREMENT
                )

                val outOfBoundsPadding = ParticleFieldDefaults.OUT_OF_BOUNDS_OFFSET_PX
                val isOutOfBounds =
                    particle.offset.x !in -outOfBoundsPadding..(canvasWidth + outOfBoundsPadding) ||
                            particle.offset.y !in -outOfBoundsPadding..(canvasHeight + outOfBoundsPadding)

                val isDead = particle.life <= ZERO_FLOAT

                if (isDead || isOutOfBounds) {
                    resetParticle(particleIndex)
                    continue
                }

                val baseHueAnimated =
                    particle.hue + elapsedSeconds * ParticleFieldDefaults.HUE_ANIMATION_SPEED_FACTOR
                val energyBoost = particle.energy - ONE_FLOAT

                val particleColor = hslToColor(
                    baseHueAnimated + energyBoost * ParticleFieldDefaults.ENERGY_BOOST_HUE_FACTOR,
                    saturation = ParticleFieldDefaults.BASE_SATURATION +
                            energyBoost * ParticleFieldDefaults.ENERGY_BOOST_SATURATION_FACTOR,
                    lightness = ParticleFieldDefaults.BASE_LIGHTNESS +
                            energyBoost * ParticleFieldDefaults.ENERGY_BOOST_LIGHTNESS_FACTOR,
                )

                val speedForTrail = particle.velocity.getDistance()
                val rawTrailLength = speedForTrail * ParticleFieldDefaults.TRAIL_SPEED_LENGTH_FACTOR
                val clampedTrailLength = min(rawTrailLength, ParticleFieldDefaults.MAX_TRAIL_LENGTH_PX)
                val trailLengthScaled = clampedTrailLength * particle.energy

                if (trailLengthScaled > ParticleFieldDefaults.TRAIL_MIN_LENGTH_THRESHOLD_PX) {
                    val trailStart = particle.offset - (particle.velocity * trailLengthScaled)

                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                particleColor.copy(alpha = ParticleFieldDefaults.TRAIL_ALPHA_ENERGY_FACTOR * particle.energy)
                            ),
                            start = trailStart,
                            end = particle.offset
                        ),
                        start = trailStart,
                        end = particle.offset,
                        strokeWidth = ParticleFieldDefaults.TRAIL_STROKE_WIDTH_ENERGY_FACTOR * particle.energy
                    )
                }

                val glowBaseSize = ParticleFieldDefaults.GLOW_SIZE_BASE_ENERGY_FACTOR * particle.energy

                drawCircle(
                    color = particleColor.copy(alpha = ParticleFieldDefaults.GLOW_LAYER1_ALPHA_ENERGY_FACTOR * particle.energy),
                    radius = glowBaseSize * ParticleFieldDefaults.GLOW_LAYER1_RADIUS_MULTIPLIER,
                    center = particle.offset
                )
                drawCircle(
                    color = particleColor.copy(alpha = ParticleFieldDefaults.GLOW_LAYER2_ALPHA_ENERGY_FACTOR * particle.energy),
                    radius = glowBaseSize, // multiplicador implícito = ONE_FLOAT
                    center = particle.offset
                )
                drawCircle(
                    color = particleColor.copy(alpha = ParticleFieldDefaults.GLOW_LAYER3_ALPHA_ENERGY_FACTOR * particle.energy),
                    radius = glowBaseSize * ParticleFieldDefaults.GLOW_LAYER3_RADIUS_MULTIPLIER,
                    center = particle.offset
                )
                drawCircle(
                    color = particleColor.copy(alpha = ParticleFieldDefaults.CORE_ALPHA),
                    radius = ParticleFieldDefaults.CORE_RADIUS_ENERGY_FACTOR * particle.energy,
                    center = particle.offset
                )
            }

            nativeCanvas.restore()
        }
    }
}
