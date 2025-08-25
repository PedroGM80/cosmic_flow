package dev.pgm.cosmic_flow.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import dev.pgm.cosmic_flow.config.SpectacularTouchRipplesDefaults
import dev.pgm.cosmic_flow.models.SpectacularRipple
import dev.pgm.cosmic_flow.utils.hslToColor
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.random.Random


@Composable
internal fun SpectacularTouchRipples(modifier: Modifier, trigger: Int, center: Offset?) {
    var ripples by remember { mutableStateOf(listOf<SpectacularRipple>()) }
    val startTime = remember { System.nanoTime() }

    LaunchedEffect(trigger) {
        center?.let {
            ripples = ripples + SpectacularRipple(
                center = it,
                progress = SpectacularTouchRipplesDefaults.INITIAL_PROGRESS,
                intensity = Random.nextFloat() * SpectacularTouchRipplesDefaults.INTENSITY_RANDOM_FACTOR + SpectacularTouchRipplesDefaults.INTENSITY_BASE,
                hue = Random.nextFloat(),
                rings = Random.nextInt(SpectacularTouchRipplesDefaults.MIN_RINGS, SpectacularTouchRipplesDefaults.MAX_RINGS_EXCLUSIVE)
            )
        }
    }

    val time by produceState(SpectacularTouchRipplesDefaults.INITIAL_PROGRESS) {
        while (isActive) {
            value = (System.nanoTime() - startTime) / SpectacularTouchRipplesDefaults.NANOS_TO_SECONDS
            withFrameNanos { }
        }
    }

    Canvas(modifier) {
        val maxDim = size.maxDimension

        ripples = ripples.filter { it.progress < SpectacularTouchRipplesDefaults.MAX_PROGRESS }
        ripples = ripples.map { ripple ->
            val baseProgress = ripple.progress

            // Dibujar múltiples anillos con efectos
            for (ring in 0 until ripple.rings) {
                val ringDelay = ring * SpectacularTouchRipplesDefaults.RING_DELAY_FACTOR
                val ringProgress = (baseProgress - ringDelay).coerceAtLeast(SpectacularTouchRipplesDefaults.RING_PROGRESS_MIN)
                if (ringProgress <= SpectacularTouchRipplesDefaults.RING_PROGRESS_MIN) continue

                val radius = maxDim * ringProgress * ripple.intensity * (SpectacularTouchRipplesDefaults.RADIUS_BASE_FACTOR + ring * SpectacularTouchRipplesDefaults.RADIUS_RING_FACTOR)
                val alpha = ((SpectacularTouchRipplesDefaults.MAX_PROGRESS - ringProgress) * ripple.intensity * SpectacularTouchRipplesDefaults.ALPHA_INTENSITY_FACTOR)
                    .coerceIn(SpectacularTouchRipplesDefaults.ALPHA_COERCE_MIN, SpectacularTouchRipplesDefaults.ALPHA_COERCE_MAX)

                if (radius > SpectacularTouchRipplesDefaults.MIN_EFFECTIVE_RADIUS && alpha > SpectacularTouchRipplesDefaults.MIN_EFFECTIVE_ALPHA) {
                    val ringHue = (ripple.hue + ring * SpectacularTouchRipplesDefaults.RING_HUE_RING_FACTOR + time * SpectacularTouchRipplesDefaults.RING_HUE_TIME_FACTOR) % SpectacularTouchRipplesDefaults.MAX_PROGRESS
                    val color = hslToColor(ringHue, SpectacularTouchRipplesDefaults.RING_HSL_SATURATION, SpectacularTouchRipplesDefaults.RING_HSL_LIGHTNESS)

                    // Onda principal con distorsión
                    val distortion = sin(time * SpectacularTouchRipplesDefaults.DISTORTION_TIME_FACTOR + ring * SpectacularTouchRipplesDefaults.DISTORTION_RING_FACTOR) * SpectacularTouchRipplesDefaults.DISTORTION_MULTIPLIER
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        radius = radius + distortion,
                        center = ripple.center,
                        style = Stroke(width = (SpectacularTouchRipplesDefaults.STROKE_WIDTH_BASE - ring * SpectacularTouchRipplesDefaults.STROKE_WIDTH_RING_FACTOR).coerceAtLeast(SpectacularTouchRipplesDefaults.STROKE_WIDTH_MIN))
                    )

                    // Glow exterior (radio seguro ≥ 1f)
                    val glowR = max(SpectacularTouchRipplesDefaults.MIN_GLOW_RADIUS, radius * SpectacularTouchRipplesDefaults.GLOW_RADIUS_MULTIPLIER)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                color.copy(alpha = alpha * SpectacularTouchRipplesDefaults.GLOW_ALPHA_MULTIPLIER),
                                Color.Transparent
                            ),
                            center = ripple.center,
                            radius = glowR
                        ),
                        radius = glowR,
                        center = ripple.center
                    )

                    // Destellos giratorios
                    for (sparkle in 0 until SpectacularTouchRipplesDefaults.NUM_SPARKLES) {
                        val sparkleAngle = time * SpectacularTouchRipplesDefaults.SPARKLE_TIME_FACTOR + sparkle * PI / (SpectacularTouchRipplesDefaults.NUM_SPARKLES / 2) + ring * SpectacularTouchRipplesDefaults.SPARKLE_ANGLE_RING_FACTOR
                        val sparkleRadius = radius * SpectacularTouchRipplesDefaults.SPARKLE_RADIUS_MULTIPLIER
                        val sparklePos = ripple.center + Offset(
                            cos(sparkleAngle.toFloat()) * sparkleRadius,
                            sin(sparkleAngle.toFloat()) * sparkleRadius
                        )

                        drawCircle(
                            color = Color.White.copy(alpha = alpha * SpectacularTouchRipplesDefaults.SPARKLE_ALPHA_MULTIPLIER),
                            radius = SpectacularTouchRipplesDefaults.SPARKLE_BASE_RADIUS_FACTOR * ripple.intensity,
                            center = sparklePos
                        )
                    }

                    // Ondas secundarias de alta frecuencia
                    for (wave in 1..SpectacularTouchRipplesDefaults.NUM_SECONDARY_WAVES) {
                        val waveRadius = radius + sin(time * SpectacularTouchRipplesDefaults.SECONDARY_WAVE_TIME_FACTOR + wave * SpectacularTouchRipplesDefaults.SECONDARY_WAVE_WAVE_FACTOR) * SpectacularTouchRipplesDefaults.SECONDARY_WAVE_RADIUS_OFFSET
                        drawCircle(
                            color = color.copy(alpha = alpha * SpectacularTouchRipplesDefaults.SECONDARY_WAVE_ALPHA_MULTIPLIER),
                            radius = waveRadius,
                            center = ripple.center,
                            style = Stroke(width = SpectacularTouchRipplesDefaults.SECONDARY_WAVE_STROKE_WIDTH)
                        )
                    }
                }
            }

            // Explosión central inicial (radio seguro)
            if (baseProgress < SpectacularTouchRipplesDefaults.EXPLOSION_PROGRESS_THRESHOLD) {
                val explosionRadius = baseProgress * SpectacularTouchRipplesDefaults.EXPLOSION_RADIUS_FACTOR * ripple.intensity
                val safeExplosion = max(SpectacularTouchRipplesDefaults.MIN_EXPLOSION_RADIUS, explosionRadius)
                val explosionAlpha =
                    ((SpectacularTouchRipplesDefaults.EXPLOSION_PROGRESS_THRESHOLD - baseProgress) / SpectacularTouchRipplesDefaults.EXPLOSION_ALPHA_BASE_FACTOR * ripple.intensity)
                        .coerceIn(SpectacularTouchRipplesDefaults.ALPHA_COERCE_MIN, SpectacularTouchRipplesDefaults.ALPHA_COERCE_MAX)

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = explosionAlpha),
                            hslToColor(ripple.hue, SpectacularTouchRipplesDefaults.EXPLOSION_HSL_SATURATION, SpectacularTouchRipplesDefaults.EXPLOSION_HSL_LIGHTNESS).copy(alpha = explosionAlpha * SpectacularTouchRipplesDefaults.EXPLOSION_CENTER_COLOR_ALPHA_MULTIPLIER),
                            Color.Transparent
                        ),
                        center = ripple.center,
                        radius = safeExplosion
                    ),
                    radius = safeExplosion,
                    center = ripple.center
                )
            }
            ripple.copy(progress = (ripple.progress + SpectacularTouchRipplesDefaults.PROGRESS_INCREMENT).coerceAtMost(SpectacularTouchRipplesDefaults.MAX_PROGRESS))
        }
    }
}
