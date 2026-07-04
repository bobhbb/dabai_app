package com.dabai.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DabaiPrimary,
    onPrimary = DabaiOnPrimary,
    primaryContainer = DabaiPrimaryLight,
    secondary = DabaiSecondary,
    onSecondary = DabaiOnPrimary,
    secondaryContainer = DabaiSecondaryLight,
    background = DabaiBackground,
    onBackground = DabaiOnSurface,
    surface = DabaiSurface,
    onSurface = DabaiOnSurface,
    surfaceVariant = DabaiWhite,
    outline = DabaiDivider,
    outlineVariant = DabaiSubtext,
    error = HealthBad,
)

@Composable
fun DabaiTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DabaiPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DabaiTypography,
        content = content
    )
}
