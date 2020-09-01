package com.yasinkacmaz.playground.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightThemeColors = lightColors(
    primary = Color(0xFF855446),
    primaryVariant = Color(0xFF9C684B),
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF0AC9F0),
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
    primary = Color(0xFF1F1F1F),
    primaryVariant = Color(0xFF3E2723),
    secondary = Color(0xFF03DAC5),
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
fun PlayGroundTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) =
    MaterialTheme(colors = if (isDarkTheme) darkThemeColors else lightThemeColors, content = content)