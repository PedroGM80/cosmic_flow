package dev.pgm.cosmic_flow.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.pgm.cosmic_flow.config.FallbackGradientDefaults
import dev.pgm.cosmic_flow.ui.theme.DarkBlueBackground
import dev.pgm.cosmic_flow.ui.theme.TransparentLightBlue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Renders an animated gradient background as a fallback for devices without AGSL support.
 *
 * Displays a radial gradient with an animated center that moves in a circular pattern,
 * creating a smooth, organic glow effect without requiring shader support.
 *
 * Used on Android versions below Android 13 (Tiramisu).
 */
@Composable
internal fun FallbackGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition(
        label = FallbackGradientDefaults.INFINITE_TRANSITION_LABEL
    )

    val animationProgress by infiniteTransition.animateFloat(
        initialValue = FallbackGradientDefaults.ANIMATION_INITIAL_VALUE,
        targetValue = FallbackGradientDefaults.ANIMATION_TARGET_VALUE,
        animationSpec = infiniteRepeatableSpec(),
        label = FallbackGradientDefaults.FLOAT_ANIMATION_LABEL
    )

    Canvas(Modifier.fillMaxSize()) {
        // Angle (Double) using module constants and PI (not literal)
        val animatedAngle = animationProgress * FallbackGradientDefaults.TWO_FACTOR_FLOAT * PI

        // Animated center of the glow
        val centerX = getCenterX(animatedAngle)
        val centerY = getCenterY(animatedAngle)

        val computedRadius = size.maxDimension * FallbackGradientDefaults.RADIUS_MULTIPLIER
        val safeRadius = max(FallbackGradientDefaults.MIN_RADIUS_GUARD, computedRadius)

        drawRect(DarkBlueBackground)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(TransparentLightBlue, Color.Transparent),
                center = Offset(centerX, centerY),
                radius = safeRadius
            ),
            radius = computedRadius,
            center = Offset(centerX, centerY)
        )
    }
}

@Composable
private fun infiniteRepeatableSpec(): InfiniteRepeatableSpec<Float> = infiniteRepeatable(
    tween(
        durationMillis = FallbackGradientDefaults.ANIMATION_DURATION_MS,
        easing = LinearEasing
    )
)

private fun DrawScope.getCenterY(animatedAngle: Double): Float = size.height * (
        FallbackGradientDefaults.CY_OFFSET_FACTOR +
                FallbackGradientDefaults.CY_COS_FACTOR * cos(animatedAngle).toFloat()
        )

private fun DrawScope.getCenterX(animatedAngle: Double): Float = size.width * (
        FallbackGradientDefaults.CX_OFFSET_FACTOR +
                FallbackGradientDefaults.CX_SIN_FACTOR * sin(animatedAngle).toFloat()
        )