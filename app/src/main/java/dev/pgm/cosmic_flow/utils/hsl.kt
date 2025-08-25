package dev.pgm.cosmic_flow.utils

import androidx.compose.ui.graphics.Color

internal fun hsl(h: Float, s: Float, l: Float): Color {
    val hh = (h % 1f + 1f) % 1f
    val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
    val p = 2 * l - q
    fun hue2rgb(t: Float): Float {
        var tt = t
        if (tt < 0f) tt += 1f
        if (tt > 1f) tt -= 1f
        return when {
            tt < 1f / 6f -> p + (q - p) * 6f * tt
            tt < 1f / 2f -> q
            tt < 2f / 3f -> p + (q - p) * (2f / 3f - tt) * 6f
            else -> p
        }
    }
    val r = hue2rgb(hh + 1f / 3f)
    val g = hue2rgb(hh)
    val b = hue2rgb(hh - 1f / 3f)
    return Color(r, g, b)
}