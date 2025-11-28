package dev.pgm.cosmic_flow.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val SINE_X_COORD_FACTOR = 1.7f
private const val SINE_TIME_FACTOR_FOR_X = 0.6f
private const val SINE_Y_COORD_FACTOR = 1.3f
private const val SINE_TIME_FACTOR_FOR_Y = -0.7f // Note: Original was -t * 0.7f, so factor is -0.7f
private const val COSINE_X_COORD_FACTOR = 1.1f
private const val COSINE_TIME_FACTOR_FOR_X =
    -0.5f // Note: Original was -t * 0.5f, so factor is -0.5f
private const val COSINE_Y_COORD_FACTOR = 1.9f
private const val COSINE_TIME_FACTOR_FOR_Y = 0.8f

/**
 * Generates a Perlin-style noise angle for flow field calculations.
 *
 * This function combines multiple sine and cosine waves with different frequencies
 * to create a smooth, continuous noise field that varies over space and time.
 *
 * @param coordinateX The x-coordinate in the noise field.
 * @param coordinateY The y-coordinate in the noise field.
 * @param timeOffset The time offset for animation.
 * @return An angle in radians representing the flow direction at the given position and time.
 */
internal fun noiseAngle(coordinateX: Float, coordinateY: Float, timeOffset: Float): Float {
    val sineComponentSum =
        sin(coordinateX * SINE_X_COORD_FACTOR + timeOffset * SINE_TIME_FACTOR_FOR_X) + sin(coordinateY * SINE_Y_COORD_FACTOR + timeOffset * SINE_TIME_FACTOR_FOR_Y)
    val cosineComponentSum =
        cos(coordinateX * COSINE_X_COORD_FACTOR + timeOffset * COSINE_TIME_FACTOR_FOR_X) + cos(coordinateY * COSINE_Y_COORD_FACTOR + timeOffset * COSINE_TIME_FACTOR_FOR_Y)
    return atan2(sineComponentSum, cosineComponentSum)
}