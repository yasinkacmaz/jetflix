package com.yasinkacmaz.jetflix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun JetflixTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, content = content)
}
