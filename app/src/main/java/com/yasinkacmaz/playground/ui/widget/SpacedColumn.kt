package com.yasinkacmaz.playground.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.Placeable
import androidx.compose.ui.unit.Dp

@Composable
fun SpacedColumn(spaceBetween: Dp, modifier: Modifier = Modifier, children: @Composable () -> Unit) =
    Layout(children, modifier) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val space = spaceBetween.value.toInt()
        val height = placeables.sumOf(Placeable::height) + (placeables.size - 1) * space
        var yPosition = 0

        layout(constraints.maxWidth, height) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)

                yPosition += placeable.height + space
            }
        }
    }
