package com.yasinkacmaz.jetflix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val primaryLight = Color(0xFFA10008)
private val onPrimaryLight = Color(0xFFFFFFFF)
private val primaryContainerLight = Color(0xFFE50914)
private val onPrimaryContainerLight = Color(0xFFFFFFFF)
private val secondaryLight = Color(0xFF79010A)
private val onSecondaryLight = Color(0xFFFFFFFF)
private val secondaryContainerLight = Color(0xFFAC2C29)
private val onSecondaryContainerLight = Color(0xFFFFFFFF)
private val tertiaryLight = Color(0xFF79010A)
private val onTertiaryLight = Color(0xFFFFFFFF)
private val tertiaryContainerLight = Color(0xFFAC2C29)
private val onTertiaryContainerLight = Color(0xFFFFFFFF)
private val errorLight = Color(0xFFBA1A1A)
private val onErrorLight = Color(0xFFFFFFFF)
private val errorContainerLight = Color(0xFFFFDAD6)
private val onErrorContainerLight = Color(0xFF410002)
private val backgroundLight = Color(0xFFFFF8F7)
private val onBackgroundLight = Color(0xFF2A1614)
private val surfaceLight = Color(0xFFFFF8F7)
private val onSurfaceLight = Color(0xFF2A1614)
private val surfaceVariantLight = Color(0xFFFFDAD5)
private val onSurfaceVariantLight = Color(0xFF5E3F3B)
private val outlineLight = Color(0xFF936E69)
private val outlineVariantLight = Color(0xFFE9BCB6)
private val scrimLight = Color(0xFF000000)
private val inverseSurfaceLight = Color(0xFF412B28)
private val inverseOnSurfaceLight = Color(0xFFFFEDEA)
private val inversePrimaryLight = Color(0xFFFFB4AA)
private val surfaceDimLight = Color(0xFFF6D2CD)
private val surfaceBrightLight = Color(0xFFFFF8F7)
private val surfaceContainerLowestLight = Color(0xFFFFFFFF)
private val surfaceContainerLowLight = Color(0xFFFFF0EE)
private val surfaceContainerLight = Color(0xFFFFE9E6)
private val surfaceContainerHighLight = Color(0xFFFFE2DE)
private val surfaceContainerHighestLight = Color(0xFFFFDAD5)

private val primaryDark = Color(0xFFFFB4AA)
private val onPrimaryDark = Color(0xFF690003)
private val primaryContainerDark = Color(0xFFD6000F)
private val onPrimaryContainerDark = Color(0xFFFFFFFF)
private val secondaryDark = Color(0xFFFFB4AC)
private val onSecondaryDark = Color(0xFF690007)
private val secondaryContainerDark = Color(0xFF8A1114)
private val onSecondaryContainerDark = Color(0xFFFFD2CD)
private val tertiaryDark = Color(0xFFFFB4AC)
private val onTertiaryDark = Color(0xFF690007)
private val tertiaryContainerDark = Color(0xFF8A1114)
private val onTertiaryContainerDark = Color(0xFFFFD2CD)
private val errorDark = Color(0xFFFFB4AB)
private val onErrorDark = Color(0xFF690005)
private val errorContainerDark = Color(0xFF93000A)
private val onErrorContainerDark = Color(0xFFFFDAD6)
private val backgroundDark = Color(0xFF200E0C)
private val onBackgroundDark = Color(0xFFFFDAD5)
private val surfaceDark = Color(0xFF200E0C)
private val onSurfaceDark = Color(0xFFFFDAD5)
private val surfaceVariantDark = Color(0xFF5E3F3B)
private val onSurfaceVariantDark = Color(0xFFE9BCB6)
private val outlineDark = Color(0xFFAF8782)
private val outlineVariantDark = Color(0xFF5E3F3B)
private val scrimDark = Color(0xFF000000)
private val inverseSurfaceDark = Color(0xFFFFDAD5)
private val inverseOnSurfaceDark = Color(0xFF412B28)
private val inversePrimaryDark = Color(0xFFC0000C)
private val surfaceDimDark = Color(0xFF200E0C)
private val surfaceBrightDark = Color(0xFF4A3330)
private val surfaceContainerLowestDark = Color(0xFF1A0908)
private val surfaceContainerLowDark = Color(0xFF2A1614)
private val surfaceContainerDark = Color(0xFF2E1A18)
private val surfaceContainerHighDark = Color(0xFF3A2522)
private val surfaceContainerHighestDark = Color(0xFF462F2C)

val lightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun ColorScheme.imageTint(): Color = if (!isSystemInDarkTheme()) Color.Gray else Color.DarkGray
