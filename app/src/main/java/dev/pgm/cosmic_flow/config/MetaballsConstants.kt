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
uniform float3 iResolution;
uniform float iTime;
uniform float3 ball0; // xyz = position, w = radius (passed as part of float3 for simplicity, consider using float4 if radius varies more independently)
uniform float3 ball1;
uniform float3 ball2;
uniform float3 ball3;

// Helper to get ball data by index
float3 getBallPosition(int index) {
    if (index == 0) return ball0;
    if (index == 1) return ball1;
    if (index == 2) return ball2;
    if (index == 3) return ball3;
    return float3(0.0);
}

// Simplified radius - assuming w component of ball vectors stores radius
// For this shader, we use a fixed radius for field calculation for simplicity,
// but individual ball radii are used for their visual representation if needed elsewhere.
// Or, more accurately, the w component of the uniform is the radius.
// We are passing radius in the z component of the vec3 for the position, and a fixed radius for the field.
// This is a bit of a hack. A vec4 for {x,y,z,radius} would be cleaner for the uniform.
// Let's assume the z component passed in the uniform IS the radius for the field calculation here.

float fieldAt(float2 point) {
    float fieldStrength = 0.0;
    float3 balls[4] = float3[4](ball0, ball1, ball2, ball3);

    for (int i = 0; i < ${NUM_BALLS}; i++) {
        float3 ballData = balls[i]; // ballData.xy is position, ballData.z is radius
        float2 ballPosition = ballData.xy;
        float ballRadius = ballData.z; // Using the z component as radius for the field

        float distanceToBall = distance(point, ballPosition);
        // Inverse square falloff for the field, modulated by ballRadius
        // Ensure ballRadius is not zero to avoid division by zero if it were variable.
        // For a fixed radius in shader, this is simpler.
        // fieldStrength += ballRadius * ballRadius / (distanceToBall * distanceToBall);
        // A common metaball function: R^2 / d^2. If ballRadius is R.
        // Simpler metaball function: R / d
        // Smootherstep or polynomial functions are also common.
        // Using: sum_i (r_i^2 / ((x - x_i)^2 + (y - y_i)^2))
         fieldStrength += ballRadius * ballRadius / (distanceToBall*distanceToBall + 0.001); // add small epsilon to avoid div by zero if point is exactly at ball center
    }
    return fieldStrength;
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord.xy / iResolution.xy;
    uv.y = 1.0 - uv.y; // Flip Y to match typical screen coordinates

    float field = fieldAt(uv * iResolution.xy); // Pass pixel coordinates

    // Color based on field strength
    // This is a simple thresholding, more complex coloring is possible
    float threshold = 1.0;
    float smoothness = 20.0; // Controls the antialiasing of the edge

    float t = smoothstep(threshold - 1.0/smoothness, threshold + 1.0/smoothness, field);
    //float t = clamp(field,0.0,1.0);


    half3 outputColor = half3(t, t*0.5, 1.0-t) * t; // Example coloring: blue to reddish based on t
    //Make it shades of blue and purple
    // outputColor = half3(t * 0.5, t * 0.2, t); // Blues/Purples
    // outputColor = mix(half3(0.0, 0.0, 0.1), half3(0.6, 0.3, 0.8), t); // Dark blue to light purple
    outputColor = mix(half3(0.05, 0.07, 0.1), half3(0.5, 0.2, 0.7), t); // Even darker base

    return half4(outputColor, 1.0);
}
""";
}