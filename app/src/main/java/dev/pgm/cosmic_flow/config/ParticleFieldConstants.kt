package dev.pgm.cosmic_flow.config

import kotlin.math.PI

/**
 * Encapsulates default constants for the ParticleField simulation.
 */
internal object ParticleFieldDefaults {
    // region Particle Initialization
    const val PARTICLE_LIFE_MIN = 0.2f
    const val PARTICLE_LIFE_RANGE = 0.8f
    const val PARTICLE_HUE_DIVISOR_FACTOR = 1f // Hue is i.toFloat() / count.toFloat() * THIS_FACTOR
    const val PARTICLE_ENERGY_MIN = 0.2f
    const val PARTICLE_ENERGY_RANGE = 0.8f
    // endregion

    // region Time
    const val NANOS_TO_SECONDS = 1_000_000_000f
    const val FRAME_TIME_SECONDS = 1f / 60f // Assuming 60 FPS for dt calculation
    // endregion

    // region Tap Burst
    const val TAP_BURST_REPEAT_COUNT = 80
    const val TAP_BURST_ANGLE_MULTIPLIER = 2f * PI.toFloat()
    const val TAP_BURST_SPEED_MIN = 50f
    const val TAP_BURST_SPEED_RANGE = 150f
    const val TAP_BURST_OFFSET_RANGE = 40f
    const val TAP_BURST_OFFSET_SHIFT = 20f
    const val TAP_BURST_LIFE_RESET = 1f
    const val TAP_BURST_ENERGY_RESET = 2f
    // endregion

    // region Physics & Field
    const val FIELD_X_COORD_FACTOR = 0.002f
    const val FIELD_Y_COORD_FACTOR = 0.002f
    const val FIELD_T_COORD_FACTOR = 1f // time factor for noiseAngle z argument
    const val FIELD_ANGLE_T_FACTOR = 0.7f // time factor for swirl sin wave
    const val FIELD_ANGLE_SWIRL_STRENGTH = 0.15f
    const val FIELD_FORCE_MAGNITUDE_BASE = 22f
    // endregion

    // region Attraction
    const val ATTRACT_RADIUS_PX = 220f
    const val MAX_ATTRACT_FORCE_MAGNITUDE = 2600f
    const val ATTRACT_FALLOFF_BASE = 1f
    const val ATTRACT_DISTANCE_GUARD_MIN = 1f // min distance to prevent division by zero
    const val ATTRACT_FORCE_BASE_FACTOR = 4500f // Raw force factor: (factor / dist)
    // ATTRACT_RAW_MAGNITUDE_CLAMP_FACTOR is MAX_ATTRACT_FORCE_MAGNITUDE / mag
    // endregion

    // region Particle Dynamics & Boundaries
    const val VELOCITY_DAMPING_BASE = 0.965f
    const val VELOCITY_DAMPING_ENERGY_FACTOR = 0.01f
    const val MAX_PARTICLE_SPEED_PX = 350f
    const val PARTICLE_LIFE_DECREMENT = 0.002f
    const val PARTICLE_ENERGY_DECREMENT = 0.005f
    const val PARTICLE_ENERGY_MIN_GUARD = 0.1f
    const val OUT_OF_BOUNDS_OFFSET_PX = 50f // For particle reset check
    // endregion

    // region Particle Appearance - Color
    const val HUE_ANIMATION_SPEED_FACTOR = 0.05f
    const val ENERGY_BOOST_HUE_FACTOR = 0.3f
    const val BASE_SATURATION = 0.8f
    const val ENERGY_BOOST_SATURATION_FACTOR = 0.2f
    const val BASE_LIGHTNESS = 0.6f
    const val ENERGY_BOOST_LIGHTNESS_FACTOR = 0.3f
    // endregion

    // region Particle Appearance - Trail
    const val TRAIL_SPEED_LENGTH_FACTOR = 0.012f
    const val MAX_TRAIL_LENGTH_PX = 15f
    // TRAIL_ENERGY_FACTOR is pp.energy
    const val TRAIL_MIN_LENGTH_THRESHOLD_PX = 4f
    const val TRAIL_ALPHA_ENERGY_FACTOR = 0.28f
    const val TRAIL_STROKE_WIDTH_ENERGY_FACTOR = 3f
    // endregion

    // region Particle Appearance - Glow & Core
    const val GLOW_SIZE_BASE_ENERGY_FACTOR = 12f

    const val GLOW_LAYER1_ALPHA_ENERGY_FACTOR = 0.03f
    const val GLOW_LAYER1_RADIUS_MULTIPLIER = 2f

    const val GLOW_LAYER2_ALPHA_ENERGY_FACTOR = 0.08f
    // GLOW_LAYER2_RADIUS_MULTIPLIER is 1f (glowSize * 1f)

    const val GLOW_LAYER3_ALPHA_ENERGY_FACTOR = 0.15f
    const val GLOW_LAYER3_RADIUS_MULTIPLIER = 0.5f

    const val CORE_ALPHA = 0.98f
    const val CORE_RADIUS_ENERGY_FACTOR = 1.8f
    // endregion
}
