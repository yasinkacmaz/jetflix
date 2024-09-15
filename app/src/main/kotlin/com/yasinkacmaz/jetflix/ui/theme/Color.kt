package com.yasinkacmaz.jetflix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE50914),
    primaryContainer = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryContainer = Color(0xFF831010),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFE50914),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    onBackground = Color.Black,
    onSurface = Color(0xFF1C1C1C),
    onError = Color.White,
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE50914),
    primaryContainer = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryContainer = Color(0xFF831010),
    background = Color(0xFF1C1C1C),
    surface = Color(0xFF1C1C1C),
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onPrimaryContainer = Color.White,
    onSecondary = Color.White,
    onSecondaryContainer = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0xFF1C1C1C),
)

@Composable
fun ColorScheme.imageTint(): Color = if (!isSystemInDarkTheme()) Color.Gray else Color.DarkGray
