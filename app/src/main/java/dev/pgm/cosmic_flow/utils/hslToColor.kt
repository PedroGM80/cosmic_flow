package dev.pgm.cosmic_flow.utils

import androidx.compose.ui.graphics.Color


private const val ZERO = 0f
private const val ONE = 1f
private const val TWO = 2f
private const val THREE = 3f
private const val SIX = 6f

private const val ONE_SIXTH = ONE / SIX        // 1/6
private const val ONE_HALF = ONE / TWO         // 1/2
private const val TWO_THIRDS = TWO / THREE     // 2/3
private const val ONE_THIRD = ONE / THREE      // 1/3

internal fun hslToColor(hue: Float, saturation: Float, lightness: Float): Color {
    val normalizedHue = (hue % ONE + ONE) % ONE

    val chroma = if (lightness < ONE_HALF) {
        lightness * (ONE + saturation)
    } else {
        lightness + saturation - lightness * saturation
    }

    val secondaryComponent = TWO * lightness - chroma

    fun hueToRgb(tempValue: Float): Float {
        var temp = tempValue
        if (temp < ZERO) temp += ONE
        if (temp > ONE) temp -= ONE

        return when {
            temp < ONE_SIXTH -> secondaryComponent + (chroma - secondaryComponent) * SIX * temp
            temp < ONE_HALF -> chroma
            temp < TWO_THIRDS -> secondaryComponent + (chroma - secondaryComponent) * (TWO_THIRDS - temp) * SIX
            else -> secondaryComponent
        }
    }

    val red = hueToRgb(normalizedHue + ONE_THIRD)
    val green = hueToRgb(normalizedHue)
    val blue = hueToRgb(normalizedHue - ONE_THIRD)

    return Color(red, green, blue)
}
