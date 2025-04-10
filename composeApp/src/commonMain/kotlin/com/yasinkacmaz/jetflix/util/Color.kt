package com.yasinkacmaz.jetflix.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val colorRange = 0..256

fun Color.Companion.randomColor() = Color(colorRange.random(), colorRange.random(), colorRange.random())

@Composable
@Stable
fun Color.Companion.rateColor(movieRate: Double): Color = remember(movieRate) {
    when {
        movieRate <= 4.5 -> Color(0xff9c180e)
        movieRate < 7 -> Color(0xff963d09)
        movieRate < 8.5 -> Color(0xff578216)
        else -> Color(0xff0d750f)
    }
}
