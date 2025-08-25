package dev.pgm.cosmic_flow.components

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
import dev.pgm.cosmic_flow.config.FallbackGradientDefaults
import dev.pgm.cosmic_flow.ui.theme.DarkBlueBackground
import dev.pgm.cosmic_flow.ui.theme.TransparentLightBlue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin


@Composable
internal fun FallbackGradientBackground() {
    val inf = rememberInfiniteTransition(label = FallbackGradientDefaults.INFINITE_TRANSITION_LABEL)
    val t by inf.animateFloat(
        initialValue = FallbackGradientDefaults.ANIMATION_INITIAL_VALUE,
        targetValue = FallbackGradientDefaults.ANIMATION_TARGET_VALUE,
        animationSpec = infiniteRepeatable(tween(FallbackGradientDefaults.ANIMATION_DURATION_MS, easing = LinearEasing)),
        label = FallbackGradientDefaults.FLOAT_ANIMATION_LABEL
    )
    Canvas(Modifier.fillMaxSize()) {
        val angle = t * FallbackGradientDefaults.TWO_FACTOR_FLOAT * PI // t * 2f * PI results in a Double
        val cx = size.width * (FallbackGradientDefaults.CX_OFFSET_FACTOR + FallbackGradientDefaults.CX_SIN_FACTOR * sin(angle).toFloat())
        val cy = size.height * (FallbackGradientDefaults.CY_OFFSET_FACTOR + FallbackGradientDefaults.CY_COS_FACTOR * cos(angle).toFloat())
        drawRect(DarkBlueBackground)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(TransparentLightBlue, Color.Transparent),
                center = Offset(cx, cy),
                radius = max(FallbackGradientDefaults.MIN_RADIUS_GUARD, size.maxDimension * FallbackGradientDefaults.RADIUS_MULTIPLIER) // guard
            ),
            radius = size.maxDimension * FallbackGradientDefaults.RADIUS_MULTIPLIER,
            center = Offset(cx, cy)
        )
    }
}
