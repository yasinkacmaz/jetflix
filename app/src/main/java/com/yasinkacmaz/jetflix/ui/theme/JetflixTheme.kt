package com.yasinkacmaz.jetflix.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun JetflixTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) DarkThemeColors else LightThemeColors
    val typography = if (isDarkTheme) DarkTypography else LightTypography
    MaterialTheme(colors = colors, typography = typography, content = content)
}
