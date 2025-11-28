package dev.pgm.cosmic_flow.utils

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the [hslToColor] function.
 */
class HslToColorTest {

    @Test
    fun `hslToColor converts pure red correctly`() {
        val color = hslToColor(hue = 0f, saturation = 1f, lightness = 0.5f)
        assertEquals(1f, color.red, 0.01f)
        assertEquals(0f, color.green, 0.01f)
        assertEquals(0f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts pure green correctly`() {
        val color = hslToColor(hue = 0.333f, saturation = 1f, lightness = 0.5f)
        assertEquals(0f, color.red, 0.01f)
        assertEquals(1f, color.green, 0.01f)
        assertEquals(0f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts pure blue correctly`() {
        val color = hslToColor(hue = 0.667f, saturation = 1f, lightness = 0.5f)
        assertEquals(0f, color.red, 0.01f)
        assertEquals(0f, color.green, 0.01f)
        assertEquals(1f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts white correctly`() {
        val color = hslToColor(hue = 0f, saturation = 0f, lightness = 1f)
        assertEquals(1f, color.red, 0.01f)
        assertEquals(1f, color.green, 0.01f)
        assertEquals(1f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts black correctly`() {
        val color = hslToColor(hue = 0f, saturation = 0f, lightness = 0f)
        assertEquals(0f, color.red, 0.01f)
        assertEquals(0f, color.green, 0.01f)
        assertEquals(0f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts gray correctly`() {
        val color = hslToColor(hue = 0f, saturation = 0f, lightness = 0.5f)
        assertEquals(0.5f, color.red, 0.01f)
        assertEquals(0.5f, color.green, 0.01f)
        assertEquals(0.5f, color.blue, 0.01f)
    }

    @Test
    fun `hslToColor handles hue wrapping above 1`() {
        val color1 = hslToColor(hue = 0f, saturation = 1f, lightness = 0.5f)
        val color2 = hslToColor(hue = 1f, saturation = 1f, lightness = 0.5f)
        val color3 = hslToColor(hue = 2f, saturation = 1f, lightness = 0.5f)

        assertEquals(color1.red, color2.red, 0.01f)
        assertEquals(color1.green, color2.green, 0.01f)
        assertEquals(color1.blue, color2.blue, 0.01f)

        assertEquals(color1.red, color3.red, 0.01f)
        assertEquals(color1.green, color3.green, 0.01f)
        assertEquals(color1.blue, color3.blue, 0.01f)
    }

    @Test
    fun `hslToColor handles negative hue correctly`() {
        val color1 = hslToColor(hue = 0f, saturation = 1f, lightness = 0.5f)
        val color2 = hslToColor(hue = -1f, saturation = 1f, lightness = 0.5f)

        assertEquals(color1.red, color2.red, 0.01f)
        assertEquals(color1.green, color2.green, 0.01f)
        assertEquals(color1.blue, color2.blue, 0.01f)
    }

    @Test
    fun `hslToColor converts desaturated red correctly`() {
        val color = hslToColor(hue = 0f, saturation = 0.5f, lightness = 0.5f)
        // Should be pinkish/light red
        assert(color.red > color.green)
        assert(color.red > color.blue)
        assert(color.green == color.blue)
    }

    @Test
    fun `hslToColor converts dark blue correctly`() {
        val color = hslToColor(hue = 0.667f, saturation = 1f, lightness = 0.25f)
        // Should be dark blue
        assert(color.blue > color.red)
        assert(color.blue > color.green)
        assertEquals(0f, color.red, 0.01f)
        assertEquals(0f, color.green, 0.01f)
    }
}
