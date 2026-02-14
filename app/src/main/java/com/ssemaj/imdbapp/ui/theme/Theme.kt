package com.ssemaj.imdbapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    // Primary
    primary = Gold,
    onPrimary = DarkBackground,
    primaryContainer = GoldDark,
    onPrimaryContainer = OnDarkSurface,

    // Secondary
    secondary = GoldLight,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceContainerHigh,
    onSecondaryContainer = OnDarkSurface,

    // Tertiary
    tertiary = Rating,
    onTertiary = DarkBackground,
    tertiaryContainer = DarkSurfaceContainerHigh,
    onTertiaryContainer = OnDarkSurface,

    // Background & Surface
    background = DarkBackground,
    onBackground = OnDarkBackground,
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSurfaceSecondary,

    // Surface containers (Material 3)
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,

    // Outline
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    // Error
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,

    // Inverse
    inverseSurface = OnDarkBackground,
    inverseOnSurface = DarkSurface,
    inversePrimary = GoldDark,

    // Scrim
    scrim = DarkBackground
)

private val LightColorScheme = lightColorScheme(
    // Primary
    primary = GoldDark,
    onPrimary = LightSurface,
    primaryContainer = GoldLight,
    onPrimaryContainer = OnLightSurface,

    // Secondary
    secondary = Gold,
    onSecondary = LightSurface,
    secondaryContainer = LightSurfaceContainerHigh,
    onSecondaryContainer = OnLightSurface,

    // Tertiary
    tertiary = Rating,
    onTertiary = LightSurface,
    tertiaryContainer = LightSurfaceContainerHigh,
    onTertiaryContainer = OnLightSurface,

    // Background & Surface
    background = LightBackground,
    onBackground = OnLightBackground,
    surface = LightSurface,
    onSurface = OnLightSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightSurfaceSecondary,

    // Surface containers (Material 3)
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,

    // Outline
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    // Error
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    // Inverse
    inverseSurface = OnLightBackground,
    inverseOnSurface = LightSurface,
    inversePrimary = Gold,

    // Scrim
    scrim = OnLightBackground
)

// Material 3 Shape tokens
private val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ImdbAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
