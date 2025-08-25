package dev.pgm.cosmic_flow.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import dev.pgm.cosmic_flow.R
import dev.pgm.cosmic_flow.config.TitleTiltDefaults


@Composable
internal fun TitleTilt(shineX: Float, touch: Offset?) {
    val densityScale = LocalDensity.current.density

    Text(
        text = stringResource(R.string.title_tilt_subtitle),
        fontSize = integerResource(R.integer.subtitle_font_size).sp,
        color = Color.White.copy(alpha = TitleTiltDefaults.SUBTITLE_COLOR_ALPHA),
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(dimensionResource(R.dimen.subtitle_padding))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.box_padding)),
        contentAlignment = Alignment.Center
    ) {
        val tiltOffsets = remember(touch) {
            val touchXDivided = touch?.x?.let { value ->
                value / TitleTiltDefaults.TILT_TOUCH_DIVISOR
            } ?: TitleTiltDefaults.TILT_CENTER_X

            val touchYDivided = touch?.y?.let { value ->
                value / TitleTiltDefaults.TILT_TOUCH_DIVISOR
            } ?: TitleTiltDefaults.TILT_CENTER_Y

            val deltaXFromCenter = touchXDivided - TitleTiltDefaults.TILT_CENTER_X
            val deltaYFromCenter = touchYDivided - TitleTiltDefaults.TILT_CENTER_Y

            deltaXFromCenter to deltaYFromCenter
        }

        val rotationXDegrees = (-tiltOffsets.second * TitleTiltDefaults.ROTATION_X_FACTOR)
            .coerceIn(-TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES, TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES)

        val rotationYDegrees = (tiltOffsets.first * TitleTiltDefaults.ROTATION_Y_FACTOR)
            .coerceIn(-TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES, TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.title_tilt_main_title),
                fontSize = integerResource(R.integer.title_font_size).sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = integerResource(R.integer.title_letter_spacing).sp,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = TitleTiltDefaults.TITLE_COLOR_ALPHA),
                modifier = Modifier
                    .graphicsLayer {
                        rotationX = rotationXDegrees
                        rotationY = rotationYDegrees
                        cameraDistance = TitleTiltDefaults.CAMERA_DISTANCE_FACTOR * densityScale
                    }
                    .shadow(
                        elevation = dimensionResource(R.dimen.shadow_elevation),
                        ambientColor = Color.Cyan,
                        spotColor = Color.Magenta.copy(alpha = TitleTiltDefaults.SPOT_COLOR_ALPHA)
                    )
                    .drawWithContent {
                        drawContent()

                        val canvasWidthPx = size.width
                        val canvasHeightPx = size.height

                        val shineCenterX = canvasWidthPx * shineX
                        val shineCenterY = canvasHeightPx / TitleTiltDefaults.DRAW_CONTENT_CY_DIVISOR
                        val radialBrushRadius = canvasWidthPx * TitleTiltDefaults.RADIAL_BRUSH_RADIUS_FACTOR

                        val radialBrushGradient = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_WHITE_ALPHA),
                                Color.Cyan.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_CYAN_ALPHA),
                                Color.Magenta.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_MAGENTA_ALPHA),
                                Color.Transparent
                            ),
                            center = Offset(shineCenterX, shineCenterY),
                            radius = radialBrushRadius
                        )

                        drawCircle(
                            brush = radialBrushGradient,
                            radius = radialBrushRadius,
                            center = Offset(shineCenterX, shineCenterY),
                            blendMode = BlendMode.Screen
                        )
                    }
            )
        }
    }
}
