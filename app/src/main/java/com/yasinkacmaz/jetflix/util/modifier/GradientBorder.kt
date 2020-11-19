package com.yasinkacmaz.jetflix.util.modifier

import androidx.compose.animation.animate
import androidx.compose.foundation.border
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

// TODO: Is there any other way to use brush(gradient) without knowing size
fun Modifier.gradientBorder(
    colors: List<Color>,
    shape: Shape,
    borderSize: Dp,
    showBorder: Boolean
) = composed {
    val animatedColors = List(colors.size) { i ->
        animate(if (showBorder) colors[i] else colors[i])
    }
    gradientBorder(
        colors = animatedColors,
        borderSize = borderSize,
        shape = shape
    )
}

private fun Modifier.gradientBorder(
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape
) = gradientBorder(
    colors = colors,
    borderSize = borderSize,
    shape = shape
) { gradientColors, size ->
    LinearGradient(
        colors = gradientColors,
        startX = 0f,
        startY = 0f,
        endX = size.width.toFloat(),
        endY = size.height.toFloat()
    )
}

private fun Modifier.gradientBorder(
    colors: List<Color>,
    borderSize: Dp = 2.dp,
    shape: Shape,
    brushProvider: (List<Color>, IntSize) -> LinearGradient
) = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    val sizeProvider = onGloballyPositioned { size = it.size }
    sizeProvider then border(
        width = borderSize,
        brush = gradient,
        shape = shape
    )
}
