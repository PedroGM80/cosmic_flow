package dev.pgm.cosmic_flow.components

import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import dev.pgm.cosmic_flow.config.MetaballsDefaults // Added import
import dev.pgm.cosmic_flow.ui.theme.DarkBlueBackground
import kotlinx.coroutines.isActive
import kotlin.math.sin
import kotlin.random.Random


@Composable
internal fun MetaballsBackground(modifier: Modifier = Modifier) {
    LocalContext.current
    val shader = remember { RuntimeShader(MetaballsDefaults.AGSL_SHADER_CODE) } // Use constant
    val brush = remember { ShaderBrush(shader) }

    var balls by remember {
        mutableStateOf(
            List(MetaballsDefaults.NUM_BALLS) { // Use constant for NUM_BALLS
                // Ball: x, y, radius
                floatArrayOf(
                    Random.nextFloat() * MetaballsDefaults.BALL_POSITION_MULTIPLIER + MetaballsDefaults.BALL_POSITION_OFFSET, // x
                    Random.nextFloat() * MetaballsDefaults.BALL_POSITION_MULTIPLIER + MetaballsDefaults.BALL_POSITION_OFFSET, // y
                    Random.nextFloat() * MetaballsDefaults.BALL_RADIUS_RANDOM_FACTOR + MetaballsDefaults.BALL_RADIUS_BASE  // radius
                )
            }
        )
    }

    val time by produceState(MetaballsDefaults.ANIMATION_INITIAL_VALUE) { // Use constant
        val startTime = System.nanoTime()
        while (isActive) {
            value = (System.nanoTime() - startTime) / MetaballsDefaults.NANOS_TO_SECONDS // Use constant
            withFrameNanos { /* Relaunch on next frame */ }
        }
    }

    // Animate ball properties slowly
    val animationTime by produceState(MetaballsDefaults.ANIMATION_INITIAL_VALUE) { // Use constant
        val startTime = System.nanoTime()
        while(isActive) {
            // Slow down overall animation by an additional factor (e.g., 10)
            value = (System.nanoTime() - startTime).toFloat() / MetaballsDefaults.NANOS_TO_SECONDS / (MetaballsDefaults.ANIMATION_DURATION_MS / 1000f) // Adjusted for clarity
            withFrameNanos { /* recompose */ }
        }
    }


    LaunchedEffect(animationTime) {
        balls = balls.mapIndexed { index, ball ->
            // Simple oscillation for x, y. Radius could also be animated.
            val baseSpeed = 0.2f // This could also be a constant
            val speedFactor = (index % 2 + 1) * MetaballsDefaults.BALL_INITIAL_RANDOM_FACTOR // Use constant
            val angleOffset = index * (kotlin.math.PI.toFloat() / (MetaballsDefaults.NUM_BALLS / 2f)) // Stagger movement patterns

            floatArrayOf(
                (MetaballsDefaults.BALL_INITIAL_RANDOM_FACTOR + sin(animationTime * baseSpeed * speedFactor + angleOffset) * 0.35f).coerceIn(MetaballsDefaults.BALL_POSITION_OFFSET, 1f - MetaballsDefaults.BALL_POSITION_OFFSET), // x
                (MetaballsDefaults.BALL_INITIAL_RANDOM_FACTOR + kotlin.math.cos(animationTime * baseSpeed * speedFactor + angleOffset * 1.5f) * 0.35f).coerceIn(MetaballsDefaults.BALL_POSITION_OFFSET, 1f - MetaballsDefaults.BALL_POSITION_OFFSET), // y
                ball[2] // Keep radius for now, or animate it: (MetaballsDefaults.BALL_RADIUS_BASE + (sin(animationTime + angleOffset) * 0.05f + 0.05f)).coerceIn(0.1f, 0.25f)
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        shader.setFloatUniform("iResolution", size.width, size.height)
        shader.setFloatUniform("iTime", time)

        balls.forEachIndexed { index, ballData ->
            // Uniform names are ball0, ball1, ball2, ball3
            // Data: x, y, radius. Shader expects (x*width, y*height, radius*min(width,height))
            // We need to scale positions to pixel space and radius appropriately.
            val shaderX = ballData[0] * size.width
            val shaderY = ballData[1] * size.height
            // Scale radius relative to the smaller dimension of the canvas to keep proportions somewhat consistent
            val shaderRadius = ballData[2] * kotlin.math.min(size.width, size.height)
            shader.setFloatUniform("ball$index", shaderX, shaderY, shaderRadius)
        }

        // Fallback color if shader is not supported or fails
        drawRect(SolidColor(DarkBlueBackground))
        drawRect(brush)
    }
}
