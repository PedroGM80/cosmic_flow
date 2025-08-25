package dev.pgm.cosmic_flow.models

import androidx.compose.ui.geometry.Offset

internal data class Particle(
    var offset: Offset,
    var velocity: Offset,
    var life: Float,
    val hue: Float,
    var energy: Float = 1f
)