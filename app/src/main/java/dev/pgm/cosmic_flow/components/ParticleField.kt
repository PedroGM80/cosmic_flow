package dev.pgm.cosmic_flow.components

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
import dev.pgm.cosmic_flow.utils.hsl
import dev.pgm.cosmic_flow.utils.noiseAngle
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random


@Composable
internal fun ParticleField(
    modifier: Modifier,
    count: Int,
    touch: Offset?,
    tapTrigger: Int
) {
    val particles = remember(count) {
        List(count) { i ->
            Particle(
                p = Offset.Zero,
                v = Offset.Zero,
                life = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_LIFE_RANGE + ParticleFieldDefaults.PARTICLE_LIFE_MIN,
                hue = (i.toFloat() / count.toFloat()) * ParticleFieldDefaults.PARTICLE_HUE_DIVISOR_FACTOR,
                energy = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_ENERGY_RANGE + ParticleFieldDefaults.PARTICLE_ENERGY_MIN
            )
        }
    }
    val startNanos = remember { System.nanoTime() }

    val frame by produceState(0L) {
        while (isActive) withFrameNanos { value = it }
    }

    LaunchedEffect(tapTrigger, touch) {
        touch ?: return@LaunchedEffect
        repeat(ParticleFieldDefaults.TAP_BURST_REPEAT_COUNT) {
            val idx = Random.nextInt(particles.size)
            val angle = Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_ANGLE_MULTIPLIER
            val speed = Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_SPEED_RANGE + ParticleFieldDefaults.TAP_BURST_SPEED_MIN
            particles[idx].p = touch + Offset(
                Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_OFFSET_RANGE - ParticleFieldDefaults.TAP_BURST_OFFSET_SHIFT,
                Random.nextFloat() * ParticleFieldDefaults.TAP_BURST_OFFSET_RANGE - ParticleFieldDefaults.TAP_BURST_OFFSET_SHIFT
            )
            particles[idx].v = Offset(
                cos(angle) * speed,
                sin(angle) * speed
            )
            particles[idx].life = ParticleFieldDefaults.TAP_BURST_LIFE_RESET
            particles[idx].energy = ParticleFieldDefaults.TAP_BURST_ENERGY_RESET
        }
    }

    Canvas(modifier) {
        val f = frame // Keep frame dependency for redraw
        val w = size.width
        val h = size.height

        fun reset(i: Int) {
            particles[i].p = Offset(Random.nextFloat() * w, Random.nextFloat() * h)
            particles[i].v = Offset.Zero
            particles[i].life = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_LIFE_RANGE + ParticleFieldDefaults.PARTICLE_LIFE_MIN // Re-randomize life
            particles[i].energy = Random.nextFloat() * ParticleFieldDefaults.PARTICLE_ENERGY_RANGE + ParticleFieldDefaults.PARTICLE_ENERGY_MIN // Re-randomize energy
        }

        particles.indices.forEach { if (particles[it].p == Offset.Zero) reset(it) }

        val now = System.nanoTime()
        val t = (now - startNanos) / ParticleFieldDefaults.NANOS_TO_SECONDS
        val dt = ParticleFieldDefaults.FRAME_TIME_SECONDS

        drawIntoCanvas { canvas ->
            val frameworkPaint = Paint().apply {
                isAntiAlias = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    blendMode = android.graphics.BlendMode.PLUS
                }
            }
            val native = canvas.nativeCanvas
            native.saveLayer(null, frameworkPaint)

            for (i in particles.indices) {
                val pp = particles[i]

                val angleNoise = noiseAngle(
                    pp.p.x * ParticleFieldDefaults.FIELD_X_COORD_FACTOR,
                    pp.p.y * ParticleFieldDefaults.FIELD_Y_COORD_FACTOR,
                    t * ParticleFieldDefaults.FIELD_T_COORD_FACTOR
                )
                val swirl = ParticleFieldDefaults.FIELD_ANGLE_SWIRL_STRENGTH * sin(t * ParticleFieldDefaults.FIELD_ANGLE_T_FACTOR)
                val totalAngle = angleNoise + swirl
                val force = Offset(
                    cos(totalAngle),
                    sin(totalAngle)
                ) * ParticleFieldDefaults.FIELD_FORCE_MAGNITUDE_BASE * pp.energy

                val attract = touch?.let { tPos ->
                    val dir = tPos - pp.p
                    val dist = max(ParticleFieldDefaults.ATTRACT_DISTANCE_GUARD_MIN, dir.getDistance())
                    val falloff = (ParticleFieldDefaults.ATTRACT_FALLOFF_BASE - (dist / ParticleFieldDefaults.ATTRACT_RADIUS_PX)).coerceIn(0f, 1f)
                    if (falloff <= 0f) {
                        Offset.Zero
                    } else {
                        val rawForce = (dir / dist) * (ParticleFieldDefaults.ATTRACT_FORCE_BASE_FACTOR / dist) * falloff * pp.energy
                        val mag = rawForce.getDistance()
                        if (mag > ParticleFieldDefaults.MAX_ATTRACT_FORCE_MAGNITUDE) rawForce * (ParticleFieldDefaults.MAX_ATTRACT_FORCE_MAGNITUDE / mag) else rawForce
                    }
                } ?: Offset.Zero

                val damping = ParticleFieldDefaults.VELOCITY_DAMPING_BASE - pp.energy * ParticleFieldDefaults.VELOCITY_DAMPING_ENERGY_FACTOR
                pp.v = (pp.v + (force + attract) * dt) * damping

                val vMag = pp.v.getDistance()
                if (vMag > ParticleFieldDefaults.MAX_PARTICLE_SPEED_PX) {
                    pp.v = pp.v * (ParticleFieldDefaults.MAX_PARTICLE_SPEED_PX / vMag)
                }

                pp.p += pp.v * dt
                pp.life -= ParticleFieldDefaults.PARTICLE_LIFE_DECREMENT
                pp.energy = max(ParticleFieldDefaults.PARTICLE_ENERGY_MIN_GUARD, pp.energy - ParticleFieldDefaults.PARTICLE_ENERGY_DECREMENT)

                val oobOffset = ParticleFieldDefaults.OUT_OF_BOUNDS_OFFSET_PX
                if (pp.life <= 0f || pp.p.x !in -oobOffset..(w + oobOffset) || pp.p.y !in -oobOffset..(h + oobOffset)) {
                    reset(i); continue
                }

                val baseHue = pp.hue + t * ParticleFieldDefaults.HUE_ANIMATION_SPEED_FACTOR
                val energyBoost = pp.energy - 1f // Assuming energy > 1 is a "boost"
                val c = hsl(
                    hue = baseHue + energyBoost * ParticleFieldDefaults.ENERGY_BOOST_HUE_FACTOR,
                    saturation = ParticleFieldDefaults.BASE_SATURATION + energyBoost * ParticleFieldDefaults.ENERGY_BOOST_SATURATION_FACTOR,
                    lightness = ParticleFieldDefaults.BASE_LIGHTNESS + energyBoost * ParticleFieldDefaults.ENERGY_BOOST_LIGHTNESS_FACTOR
                )

                val speed = pp.v.getDistance()
                val trailLengthValue = min(
                    speed * ParticleFieldDefaults.TRAIL_SPEED_LENGTH_FACTOR,
                    ParticleFieldDefaults.MAX_TRAIL_LENGTH_PX
                ) * pp.energy

                if (trailLengthValue > ParticleFieldDefaults.TRAIL_MIN_LENGTH_THRESHOLD_PX) {
                    val trailStart = pp.p - (pp.v * trailLengthValue)
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                c.copy(alpha = ParticleFieldDefaults.TRAIL_ALPHA_ENERGY_FACTOR * pp.energy)
                            ),
                            start = trailStart,
                            end = pp.p
                        ),
                        start = trailStart,
                        end = pp.p,
                        strokeWidth = ParticleFieldDefaults.TRAIL_STROKE_WIDTH_ENERGY_FACTOR * pp.energy
                    )
                }

                val glowSize = ParticleFieldDefaults.GLOW_SIZE_BASE_ENERGY_FACTOR * pp.energy
                drawCircle(
                    color = c.copy(alpha = ParticleFieldDefaults.GLOW_LAYER1_ALPHA_ENERGY_FACTOR * pp.energy),
                    radius = glowSize * ParticleFieldDefaults.GLOW_LAYER1_RADIUS_MULTIPLIER,
                    center = pp.p
                )
                drawCircle(
                    color = c.copy(alpha = ParticleFieldDefaults.GLOW_LAYER2_ALPHA_ENERGY_FACTOR * pp.energy),
                    radius = glowSize, // Implicit GLOW_LAYER2_RADIUS_MULTIPLIER = 1f
                    center = pp.p
                )
                drawCircle(
                    color = c.copy(alpha = ParticleFieldDefaults.GLOW_LAYER3_ALPHA_ENERGY_FACTOR * pp.energy),
                    radius = glowSize * ParticleFieldDefaults.GLOW_LAYER3_RADIUS_MULTIPLIER,
                    center = pp.p
                )
                drawCircle(
                    color = c.copy(alpha = ParticleFieldDefaults.CORE_ALPHA),
                    radius = ParticleFieldDefaults.CORE_RADIUS_ENERGY_FACTOR * pp.energy,
                    center = pp.p
                )
            }
            native.restore()
        }
    }
}
