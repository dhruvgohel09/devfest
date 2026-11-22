package com.devfest.wellnest.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Green80,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB7F0D1),
    onPrimaryContainer = Green40,
    secondary = Teal60,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Teal40,
    tertiary = Amber60,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE0B2),
    onTertiaryContainer = Amber40,
    surface = LightSurface,
    background = NeutralGray,
)

private val DarkColorScheme = darkColorScheme(
    primary = Green60,
    onPrimary = Color.Black,
    primaryContainer = Green40,
    onPrimaryContainer = Color(0xFFB7F0D1),
    secondary = Teal80,
    onSecondary = Color.Black,
    secondaryContainer = Teal40,
    onSecondaryContainer = Color(0xFFB2DFDB),
    tertiary = Amber80,
    onTertiary = Color.Black,
    tertiaryContainer = Amber40,
    onTertiaryContainer = Color(0xFFFFE0B2),
    surface = DarkSurface,
    background = DarkGray,
)

@Composable
fun WellNestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WellNestTypography,
        shapes = WellNestShapes,
        content = content
    )
}
