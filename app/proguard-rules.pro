# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ====================================
# Cosmic Flow Project-Specific Rules
# ====================================

# Keep AGSL shader code - RuntimeShader uses reflection
-keep class android.graphics.RuntimeShader { *; }
-keepclassmembers class * {
    android.graphics.RuntimeShader *;
}

# Keep shader-related constants and classes
-keepclassmembers class dev.pgm.cosmic_flow.config.MetaballsDefaults {
    public static final java.lang.String AGSL_SHADER_CODE;
}

# Keep all model classes used in state
-keep class dev.pgm.cosmic_flow.models.** { *; }

# Keep Compose runtime for reflection
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.graphics.** { *; }

# Keep utility functions that might be called via reflection
-keep class dev.pgm.cosmic_flow.utils.** { *; }

# Preserve annotations for Compose
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# Keep component constructors for Compose preview
-keepclasseswithmembers class * {
    @androidx.compose.ui.tooling.preview.Preview <methods>;
}

# Prevent optimization of @Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep R class and all inner classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep BuildConfig
-keep class dev.pgm.cosmic_flow.BuildConfig { *; }

# Prevent stripping of debug information in release builds
# This helps with stack traces while keeping code optimized
-keepattributes LineNumberTable,SourceFile

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}