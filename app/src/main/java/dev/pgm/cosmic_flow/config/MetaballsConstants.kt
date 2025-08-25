package dev.pgm.cosmic_flow.config

internal object MetaballsDefaults {
    const val NUM_BALLS = 4
    const val ANIMATION_INITIAL_VALUE = 0f
    const val ANIMATION_TARGET_VALUE = 1f
    const val ANIMATION_DURATION_MS = 10_000 // Animation duration in milliseconds
    const val NANOS_TO_SECONDS = 1_000_000_000f

    // Ball properties
    const val BALL_RADIUS_BASE = 0.15f
    const val BALL_RADIUS_RANDOM_FACTOR = 0.1f
    const val BALL_POSITION_MULTIPLIER = 0.8f
    const val BALL_POSITION_OFFSET = 0.1f
    const val BALL_INITIAL_RANDOM_FACTOR = 0.5f // General random factor for initial positions

    // Ball animation properties in LaunchedEffect
    const val ANIMATION_BASE_SPEED = 0.2f
    const val ANIMATION_SPEED_FACTOR_MULTIPLIER = 0.5f
    const val ANIMATION_ANGLE_OFFSET_DIVISOR = 2.0f // PI / this value for angle offset
    const val ANIMATION_AMPLITUDE = 0.35f
    const val ANIMATION_MAX_POSITION_BOUND = 1.0f // Used for coerceIn upper limit: MAX_BOUND - OFFSET
    const val ANIMATION_COS_ANGLE_MULTIPLIER = 1.5f


    const val AGSL_SHADER_CODE = """
uniform float2 iResolution;
uniform float iTime;

uniform float3 ball0;
uniform float3 ball1;
uniform float3 ball2;
uniform float3 ball3;

float3 getBall(int i) {
    if (i == 0) return ball0;
    if (i == 1) return ball1;
    if (i == 2) return ball2;
    return ball3;
}

half4 main(float2 fragCoord) {
    float2 point = fragCoord;
    float fieldStrength = 0.0;

    for (int i = 0; i < 4; i++) {
        float3 ball = getBall(i);
        float2 ballPosition = ball.xy;
        float ballRadius = ball.z;

        float distanceToBall = distance(point, ballPosition);
        fieldStrength += ballRadius * ballRadius / (distanceToBall * distanceToBall + 0.001);
    }

    float threshold = 1.0;
    float alpha = smoothstep(threshold - 0.1, threshold + 0.1, fieldStrength);

    return half4(0.0, 0.7, 1.0, alpha);
}
"""

}