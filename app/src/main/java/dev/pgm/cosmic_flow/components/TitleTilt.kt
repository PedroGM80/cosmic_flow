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
    val density = LocalDensity.current.density

    Text(
        text = stringResource(R.string.title_tilt_subtitle),
        fontSize = integerResource(R.integer.subtitle_font_size).sp,
        color = Color.White.copy(alpha = TitleTiltDefaults.SUBTITLE_COLOR_ALPHA),
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(dimensionResource(R.dimen.subtitle_padding))
    )

    Box(
        Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.box_padding)),
        contentAlignment = Alignment.Center
    ) {
        val tilt = remember(touch) {
            val px = touch?.x?.let { it / TitleTiltDefaults.TILT_TOUCH_DIVISOR } ?: TitleTiltDefaults.TILT_CENTER_X
            val py = touch?.y?.let { it / TitleTiltDefaults.TILT_TOUCH_DIVISOR } ?: TitleTiltDefaults.TILT_CENTER_Y
            val dx = px - TitleTiltDefaults.TILT_CENTER_X
            val dy = py - TitleTiltDefaults.TILT_CENTER_Y
            dx to dy
        }

        val rotX = (-tilt.second * TitleTiltDefaults.ROTATION_X_FACTOR).coerceIn(-TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES, TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES)
        val rotY = (tilt.first * TitleTiltDefaults.ROTATION_Y_FACTOR).coerceIn(-TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES, TitleTiltDefaults.MIN_MAX_ROTATION_DEGREES)

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
                        rotationX = rotX
                        rotationY = rotY
                        cameraDistance = TitleTiltDefaults.CAMERA_DISTANCE_FACTOR * density
                    }
                    .shadow(
                        elevation = dimensionResource(R.dimen.shadow_elevation),
                        ambientColor = Color.Cyan,
                        spotColor = Color.Magenta.copy(alpha = TitleTiltDefaults.SPOT_COLOR_ALPHA)
                    )
                    .drawWithContent {
                        drawContent()

                        val w = size.width
                        val h = size.height
                        val cx = w * shineX
                        val cy = h / TitleTiltDefaults.DRAW_CONTENT_CY_DIVISOR
                        val radius = w * TitleTiltDefaults.RADIAL_BRUSH_RADIUS_FACTOR

                        val radial = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_WHITE_ALPHA),
                                Color.Cyan.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_CYAN_ALPHA),
                                Color.Magenta.copy(alpha = TitleTiltDefaults.RADIAL_BRUSH_COLOR_MAGENTA_ALPHA),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = radius
                        )

                        drawCircle(
                            brush = radial,
                            radius = radius,
                            center = Offset(cx, cy),
                            blendMode = BlendMode.Screen
                        )
                    }
            )
        }
    }
}
