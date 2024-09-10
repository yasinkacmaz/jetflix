package com.yasinkacmaz.jetflix.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
val LightThemeColors = lightColorScheme(
    primary = Color(0xFFE50914),
    primaryContainer = Color(0xFFE50914),
    secondary = Color(0xFF971C1C),
    secondaryContainer = Color(0xFF971C1C),
    tertiary = Color(0xFFE50914),
    tertiaryContainer = Color(0xFFE50914),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFE50914),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color(0xFF1C1C1C),
    onError = Color.White,
)

val DarkThemeColors = darkColorScheme(
    primary = Color(0xFFE50914),
    primaryContainer = Color(0xFFE50914),
    secondary = Color(0xFF971C1C),
    secondaryContainer = Color(0xFF971C1C),
    tertiary = Color(0xFFE50914),
    tertiaryContainer = Color(0xFFE50914),
    background = Color(0xFF1C1C1C),
    surface = Color(0xFF1C1C1C),
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0xFF1C1C1C),
)

@Composable
fun ColorScheme.imageTint(): Color = if (!isSystemInDarkTheme()) Color.Gray else Color.DarkGray
