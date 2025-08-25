package dev.pgm.cosmic_flow.config

internal object SpectacularTouchRipplesDefaults {
    // Ripple generation
    const val INITIAL_PROGRESS = 0f
    const val INTENSITY_RANDOM_FACTOR = 0.5f
    const val INTENSITY_BASE = 1f
    const val MIN_RINGS = 3
    const val MAX_RINGS_EXCLUSIVE = 8 // Random.nextInt(min, max)

    // Time
    const val NANOS_TO_SECONDS = 1_000_000_000f

    // Ripple state
    const val MAX_PROGRESS = 1f

    // Ring effect
    const val RING_DELAY_FACTOR = 0.1f
    const val RING_PROGRESS_MIN = 0f
    const val RADIUS_BASE_FACTOR = 0.4f
    const val RADIUS_RING_FACTOR = 0.1f
    const val ALPHA_INTENSITY_FACTOR = 0.4f
    const val ALPHA_COERCE_MIN = 0f
    const val ALPHA_COERCE_MAX = 1f
    const val MIN_EFFECTIVE_RADIUS = 0f
    const val MIN_EFFECTIVE_ALPHA = 0.01f

    // Ring appearance
    const val RING_HUE_RING_FACTOR = 0.15f
    const val RING_HUE_TIME_FACTOR = 0.2f
    const val RING_HSL_SATURATION = 0.8f
    const val RING_HSL_LIGHTNESS = 0.6f

    // Main wave distortion
    const val DISTORTION_TIME_FACTOR = 8f
    const val DISTORTION_RING_FACTOR = 2f
    const val DISTORTION_MULTIPLIER = 3f
    const val STROKE_WIDTH_BASE = 4f
    const val STROKE_WIDTH_RING_FACTOR = 0.3f
    const val STROKE_WIDTH_MIN = 1f

    // Glow effect
    const val MIN_GLOW_RADIUS = 1f
    const val GLOW_RADIUS_MULTIPLIER = 1.5f
    const val GLOW_ALPHA_MULTIPLIER = 0.3f

    // Sparkles
    const val NUM_SPARKLES = 8
    const val SPARKLE_TIME_FACTOR = 3f
    const val SPARKLE_ANGLE_RING_FACTOR = 0.5f
    const val SPARKLE_RADIUS_MULTIPLIER = 0.9f
    const val SPARKLE_BASE_RADIUS_FACTOR = 3f // Multiplied by ripple intensity
    const val SPARKLE_ALPHA_MULTIPLIER = 0.8f

    // Secondary waves
    const val NUM_SECONDARY_WAVES = 3 // loops from 1 to this value
    const val SECONDARY_WAVE_TIME_FACTOR = 12f
    const val SECONDARY_WAVE_WAVE_FACTOR = 2f
    const val SECONDARY_WAVE_RADIUS_OFFSET = 8f
    const val SECONDARY_WAVE_ALPHA_MULTIPLIER = 0.2f
    const val SECONDARY_WAVE_STROKE_WIDTH = 1.5f

    // Initial explosion
    const val EXPLOSION_PROGRESS_THRESHOLD = 0.3f
    const val EXPLOSION_RADIUS_FACTOR = 30f
    const val MIN_EXPLOSION_RADIUS = 1f
    const val EXPLOSION_ALPHA_BASE_FACTOR = 0.3f // Divisor for (threshold - progress)
    const val EXPLOSION_HSL_SATURATION = 1f
    const val EXPLOSION_HSL_LIGHTNESS = 0.8f
    const val EXPLOSION_CENTER_COLOR_ALPHA_MULTIPLIER = 0.7f

    // Progress update
    const val PROGRESS_INCREMENT = 0.008f
}
