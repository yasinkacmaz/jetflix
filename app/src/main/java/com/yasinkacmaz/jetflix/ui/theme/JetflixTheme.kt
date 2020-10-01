package com.yasinkacmaz.jetflix.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightThemeColors = lightColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryVariant = Color(0xFF831010),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFE50914),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color(0xFF1C1C1C),
    onError = Color.White
)

val darkThemeColors = darkColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    background = Color(0xFF1C1C1C),
    surface = Color(0xFF1C1C1C),
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0x1C1C1C)
)

@Composable
fun JetflixTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) darkThemeColors else lightThemeColors
    MaterialTheme(colors = colors, content = content)
}