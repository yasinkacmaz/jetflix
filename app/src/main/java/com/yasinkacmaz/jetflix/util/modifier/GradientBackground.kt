package com.yasinkacmaz.jetflix.util.modifier

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.gradientBackground(colors: List<Color>, shape: Shape, showBackground: Boolean = true) = composed {
    val animatedColors = List(colors.size) { i ->
        animateColorAsState(if (showBackground) colors[i] else colors[i].copy(alpha = 0f), remember { spring() }).value
    }
    gradientBackground(colors = animatedColors, shape = shape)
}

private fun Modifier.gradientBackground(colors: List<Color>, shape: Shape) =
    gradientBackground(colors = colors, shape = shape) { gradientColors, size ->
        Brush.linearGradient(
            colors = gradientColors,
            start = Offset(0f, 0f),
            end = Offset(size.width.toFloat(), size.height.toFloat())
        )
    }

private fun Modifier.gradientBackground(
    colors: List<Color>,
    shape: Shape,
    brushProvider: (List<Color>, IntSize) -> Brush
) = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val gradientBrush = remember(colors, size) { brushProvider(colors, size) }
    val sizeProvider = onGloballyPositioned { size = it.size }
    sizeProvider then background(brush = gradientBrush, shape = shape)
}
