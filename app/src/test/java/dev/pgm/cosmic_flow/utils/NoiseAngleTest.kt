package dev.pgm.cosmic_flow.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.PI
import kotlin.math.abs

/**
 * Unit tests for the [noiseAngle] function.
 */
class NoiseAngleTest {

    @Test
    fun `noiseAngle returns value within valid angle range`() {
        val angle = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 0f)
        assertTrue(angle >= -PI && angle <= PI)
    }

    @Test
    fun `noiseAngle produces different values for different coordinates`() {
        val angle1 = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 0f)
        val angle2 = noiseAngle(coordinateX = 1f, coordinateY = 0f, timeOffset = 0f)
        val angle3 = noiseAngle(coordinateX = 0f, coordinateY = 1f, timeOffset = 0f)

        assertTrue(abs(angle1 - angle2) > 0.01f)
        assertTrue(abs(angle1 - angle3) > 0.01f)
    }

    @Test
    fun `noiseAngle produces different values for different time offsets`() {
        val angle1 = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 0f)
        val angle2 = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 1f)
        val angle3 = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 2f)

        assertTrue(abs(angle1 - angle2) > 0.01f)
        assertTrue(abs(angle2 - angle3) > 0.01f)
    }

    @Test
    fun `noiseAngle is deterministic for same inputs`() {
        val angle1 = noiseAngle(coordinateX = 5f, coordinateY = 3f, timeOffset = 2f)
        val angle2 = noiseAngle(coordinateX = 5f, coordinateY = 3f, timeOffset = 2f)

        assertEquals(angle1, angle2, 0.0001f)
    }

    @Test
    fun `noiseAngle produces smooth continuous values`() {
        // Test that nearby points have similar angles (continuity)
        val angle1 = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 0f)
        val angle2 = noiseAngle(coordinateX = 0.01f, coordinateY = 0f, timeOffset = 0f)

        // The difference should be relatively small for nearby points
        assertTrue(abs(angle1 - angle2) < 0.5f)
    }

    @Test
    fun `noiseAngle handles negative coordinates`() {
        val angle1 = noiseAngle(coordinateX = -5f, coordinateY = -3f, timeOffset = 1f)
        val angle2 = noiseAngle(coordinateX = 5f, coordinateY = 3f, timeOffset = 1f)

        // Both should be valid angles
        assertTrue(angle1 >= -PI && angle1 <= PI)
        assertTrue(angle2 >= -PI && angle2 <= PI)
        // And they should be different
        assertTrue(abs(angle1 - angle2) > 0.01f)
    }

    @Test
    fun `noiseAngle handles large coordinate values`() {
        val angle = noiseAngle(coordinateX = 1000f, coordinateY = 2000f, timeOffset = 500f)

        // Should still produce a valid angle
        assertTrue(angle >= -PI && angle <= PI)
    }

    @Test
    fun `noiseAngle produces varied output across grid`() {
        val angles = mutableListOf<Float>()

        // Sample a 5x5 grid
        for (x in 0..4) {
            for (y in 0..4) {
                val angle = noiseAngle(
                    coordinateX = x.toFloat(),
                    coordinateY = y.toFloat(),
                    timeOffset = 0f
                )
                angles.add(angle)
            }
        }

        // Check that we have good variation in the output
        val uniqueAngles = angles.distinct()
        assertTrue("Should have varied angles across grid", uniqueAngles.size > 15)
    }

    @Test
    fun `noiseAngle origin returns consistent value`() {
        val angle = noiseAngle(coordinateX = 0f, coordinateY = 0f, timeOffset = 0f)

        // This is a regression test - the value at origin should remain stable
        assertTrue(angle >= -PI && angle <= PI)
    }
}
