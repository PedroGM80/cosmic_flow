package dev.pgm.cosmic_flow.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

internal fun noiseAngle(x: Float, y: Float, t: Float): Float {
    val s = sin(x * 1.7f + t * 0.6f) + sin(y * 1.3f - t * 0.7f)
    val c = cos(x * 1.1f - t * 0.5f) + cos(y * 1.9f + t * 0.8f)
    return atan2(s, c)
}