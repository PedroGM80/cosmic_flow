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

/**
 * Converts HSL (Hue, Saturation, Lightness) color values to a Compose [Color].
 *
 * @param hue The hue component (0.0-1.0). Values outside this range will be wrapped.
 * @param saturation The saturation component (0.0-1.0). 0 is grayscale, 1 is full color.
 * @param lightness The lightness component (0.0-1.0). 0 is black, 0.5 is pure color, 1 is white.
 * @return A [Color] object with the RGB values corresponding to the HSL input.
 */
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
