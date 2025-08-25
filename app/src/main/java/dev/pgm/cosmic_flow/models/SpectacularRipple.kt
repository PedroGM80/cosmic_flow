package dev.pgm.cosmic_flow.models

import androidx.compose.ui.geometry.Offset

internal data class SpectacularRipple(
    val center: Offset,
    var progress: Float,
    val intensity: Float,
    val hue: Float,
    val rings: Int
)