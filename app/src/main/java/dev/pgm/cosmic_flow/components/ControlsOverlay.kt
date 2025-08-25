package dev.pgm.cosmic_flow.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.pgm.cosmic_flow.R
import dev.pgm.cosmic_flow.config.ControlsOverlayDefaults


@Composable
internal fun ControlsOverlay(particles: Int) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Surface(
            color = ControlsOverlayDefaults.SurfaceBackgroundColor,
            tonalElevation = dimensionResource(id = R.dimen.control_overlay_surface_elevation),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.control_overlay_corner_radius)),
            modifier = Modifier.padding(dimensionResource(id = R.dimen.control_overlay_surface_padding))
        ) {
            Text(
                text = stringResource(id = R.string.control_overlay_info_text, particles),
                color = Color.White.copy(alpha = ControlsOverlayDefaults.TextColorAlpha),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.control_overlay_text_padding_horizontal),
                    vertical = dimensionResource(id = R.dimen.control_overlay_text_padding_vertical)
                )
            )
        }
    }
}
