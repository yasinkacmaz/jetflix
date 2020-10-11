package com.yasinkacmaz.jetflix.util

import androidx.compose.ui.graphics.Color

private val colorRange = 0..256

fun Color.Companion.randomColor() = Color(colorRange.random(), colorRange.random(), colorRange.random())
