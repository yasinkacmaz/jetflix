package com.yasinkacmaz.playground

import androidx.compose.Composable
import androidx.ui.material.MaterialColors
import androidx.ui.material.MaterialTheme
import androidx.ui.material.MaterialTypography

@Composable
fun PlaygroundTheme(children: @Composable() () -> Unit) {
    val playgroundColors = MaterialColors()
    val typography = MaterialTypography()
    MaterialTheme(colors = playgroundColors, typography = typography, children = children)
}
