@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalGraphicsApi::class)

package dev.pgm.cosmic_flow

import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.pgm.cosmic_flow.components.ControlsOverlay
import dev.pgm.cosmic_flow.components.FallbackGradientBackground
import dev.pgm.cosmic_flow.components.MetaballsBackground
import dev.pgm.cosmic_flow.components.ParticleField
import dev.pgm.cosmic_flow.components.SpectacularTouchRipples
import dev.pgm.cosmic_flow.components.TitleTilt

private const val DEFAULT_PARTICLE_COUNT = 720
private const val SHINE_ANIM_INITIAL_VALUE = -1f
private const val SHINE_ANIM_TARGET_VALUE = 2f
private const val SHINE_ANIM_DURATION_MS = 3800
private const val INFINITE_TRANSITION_LABEL = "inf"
private const val SHINE_ANIM_LABEL = "shine"
private const val INITIAL_TAP_RIPPLE_TRIGGER = 0

/**
 * Main screen composable for the Cosmic Flow application.
 *
 * Displays an interactive particle field with metaballs background (on Android 13+),
 * touch ripple effects, and an animated title. Responds to drag and tap gestures.
 *
 * @param modifier The modifier to apply to the screen container.
 * @param particleCount The number of particles to render in the field. Default is 720.
 * @param useShaderBackground Whether to use the AGSL shader background (requires Android 13+).
 *                            Falls back to gradient background on older devices.
 */
@Composable
fun CosmicFlowScreen(
    modifier: Modifier = Modifier,
    particleCount: Int = DEFAULT_PARTICLE_COUNT,
    useShaderBackground: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
) {
    var touch by remember { mutableStateOf<Offset?>(null) }
    var tapRippleTrigger by remember { mutableIntStateOf(INITIAL_TAP_RIPPLE_TRIGGER) }

    val particleFieldDescription = stringResource(R.string.cd_particle_field)
    val backgroundDescription = stringResource(R.string.cd_background)

    val inf = rememberInfiniteTransition(label = INFINITE_TRANSITION_LABEL)
    val shineX by inf.animateFloat(
        initialValue = SHINE_ANIM_INITIAL_VALUE,
        targetValue = SHINE_ANIM_TARGET_VALUE,
        animationSpec = infiniteRepeatable(tween(SHINE_ANIM_DURATION_MS, easing = LinearEasing)),
        label = SHINE_ANIM_LABEL
    )

    Box(
        modifier
            .fillMaxSize()
            .background(Color.Black)
            .semantics {
                contentDescription = particleFieldDescription
            }
            // Continuous DRAG: updates 'touch' in real-time
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touch = offset },
                    onDrag = { change, _ ->
                        touch = change.position
                        change.consume()
                    },
                    onDragEnd = { touch = null },
                    onDragCancel = { touch = null }
                )
            }
            // TAP: only for ripple waves
            .pointerInput(Unit) {
                detectTapGestures { pos ->
                    touch = pos
                    tapRippleTrigger++
                }
            }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = backgroundDescription
                }
        ) {
            if (useShaderBackground && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                MetaballsBackground()
            } else {
                FallbackGradientBackground()
            }
        }

        ParticleField(
            modifier = Modifier.fillMaxSize(),
            count = particleCount,
            touch = touch,
            tapTrigger = tapRippleTrigger
        )

        SpectacularTouchRipples(
            modifier = Modifier.fillMaxSize(),
            trigger = tapRippleTrigger,
            center = touch
        )

        TitleTilt(shineX = shineX, touch = touch)
        ControlsOverlay(particleCount)
    }
}