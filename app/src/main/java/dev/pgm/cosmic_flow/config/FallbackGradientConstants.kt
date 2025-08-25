package dev.pgm.cosmic_flow.config

internal object FallbackGradientDefaults {
    const val ANIMATION_INITIAL_VALUE = 0f
    const val ANIMATION_TARGET_VALUE = 1f
    const val ANIMATION_DURATION_MS = 9000
    const val INFINITE_TRANSITION_LABEL = "bg"
    const val FLOAT_ANIMATION_LABEL = "t"
    const val CX_OFFSET_FACTOR = 0.3f
    const val CX_SIN_FACTOR = 0.4f
    const val CY_OFFSET_FACTOR = 0.4f
    const val CY_COS_FACTOR = 0.3f
    const val TWO_FACTOR_FLOAT = 2f // Factor to multiply with PI
    const val RADIUS_MULTIPLIER = 0.9f
    const val MIN_RADIUS_GUARD = 1f
}
