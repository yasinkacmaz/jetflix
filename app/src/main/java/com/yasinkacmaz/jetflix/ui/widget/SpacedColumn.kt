package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp

@Composable
fun SpacedColumn(spaceBetween: Dp, modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    val space = (spaceBetween.value * DensityAmbient.current.density).toInt()
    Layout(children, modifier) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(0, 0) { }
        }
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val height = placeables.sumOf(Placeable::height) + (placeables.size - 1) * space
        var yPosition = 0

        layout(constraints.maxWidth, height) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height + space
            }
        }
    }
}
