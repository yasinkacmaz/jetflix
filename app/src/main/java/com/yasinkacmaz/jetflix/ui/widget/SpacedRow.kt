package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import com.yasinkacmaz.jetflix.util.toPx

// TODO: Why I cant use DominantColorAmbient with this layout?
// TODO: Maybe use placeable.placeRelative
@Composable
fun SpacedRow(spaceBetween: Dp, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val space = spaceBetween.toPx().toInt()
    Layout(content, modifier) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(0, 0) { }
        }
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val width = placeables.sumOf(Placeable::width) + (placeables.size - 1) * space
        var xPosition = 0

        layout(width, placeables.maxOf(Placeable::height)) {
            placeables.forEach { placeable ->
                placeable.place(x = xPosition, y = 0)
                xPosition += placeable.width + space
            }
        }
    }
}
