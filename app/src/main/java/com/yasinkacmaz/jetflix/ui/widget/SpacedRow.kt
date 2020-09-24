package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.Placeable
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp

@Composable
fun SpacedRow(spaceBetween: Dp, modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    val space = (spaceBetween.value * DensityAmbient.current.density).toInt()
    Layout(children, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val width = placeables.sumOf(Placeable::width) + (placeables.size - 1) * space
        var xPosition = 0

        layout(width, placeables.maxOf(Placeable::height)) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = 0)
                xPosition += placeable.width + space
            }
        }
    }
}
