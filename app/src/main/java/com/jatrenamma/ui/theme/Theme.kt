package com.jatrenamma.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val JatreDarkColorScheme = darkColorScheme(
    primary          = JatreGold,
    onPrimary        = JatreBlueDark,
    primaryContainer = JatreBlue,
    onPrimaryContainer = JatreSilverLight,
    secondary        = JatreSilver,
    onSecondary      = JatreBlueDark,
    background       = JatreBlueDark,
    onBackground     = JatreWhite,
    surface          = CardSurface,
    onSurface        = JatreWhite,
    surfaceVariant   = CardBorder,
    onSurfaceVariant = JatreSilver,
    error            = JatreError,
    outline          = CardBorder
)

@Composable
fun JatreNammaTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = JatreBlueDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = JatreDarkColorScheme,
        typography  = JatreTypography,
        content     = content
    )
}
