package com.yasinkacmaz.playground.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightThemeColors = lightColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF7D0A10),
    secondary = Color(0xFFD9D4D5),
    secondaryVariant = Color(0xFFB8B0B2),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

val darkThemeColors = darkColors(
    primary = Color(0xFF6441A5),
    primaryVariant = Color(0xFF4C327D),
    secondary = Color(0xFFA429BA),
    background = Color(0xFF121212),
    surface = Color.Black,
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun PlayGroundTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) darkThemeColors else lightThemeColors
    MaterialTheme(colors = colors, content = content)
}