package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.util.randomColor
import kotlin.math.ceil

@Composable
fun VerticalStaggeredGrid(
    modifier: Modifier = Modifier,
    columnCount: Int,
    columnSpacing: Dp = 0.dp,
    rowSpacing: Dp = 0.dp,
    children: @Composable () -> Unit
) {
    Layout(
        children = children,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) { measurables, constraints ->
        val rowSpacingPx = rowSpacing.toIntPx()
        val columnSpacingPx = columnSpacing.toIntPx()
        val totalColumnSpacing = (columnCount - 1) * columnSpacingPx
        val rowCount = ceil(measurables.count() / columnCount.toFloat()).toInt()
        val itemWidth = (constraints.maxWidth - totalColumnSpacing) / columnCount
        val itemConstraints = constraints.copy(maxWidth = itemWidth)
        val columnHeights = IntArray(columnCount) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->
            val column = shortestColumnIndex(columnHeights)
            val placeable = measurable.measure(itemConstraints)
            columnHeights[column] += placeable.height
            columnHeights[column] += if (placingToLastRow(index, rowCount, columnCount)) 0 else rowSpacingPx
            placeable
        }

        val height = columnHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(constraints.maxWidth, height ?: constraints.minHeight) {
            val columnY = IntArray(columnCount) { 0 }
            placeables.forEachIndexed { index, placeable ->
                val columnIndex = shortestColumnIndex(columnY)
                val columnPadding = (index % columnCount) * columnSpacingPx
                val placeableX = (itemWidth * columnIndex) + columnPadding
                placeable.placeRelative(x = placeableX, y = columnY[columnIndex])
                columnY[columnIndex] += placeable.height
                columnY[columnIndex] += if (placingToLastRow(index, rowCount, columnCount)) 0 else rowSpacingPx
            }
        }
    }
}

private fun shortestColumnIndex(columnHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var columnIndex = 0
    columnHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            columnIndex = index
        }
    }
    return columnIndex
}

private fun placingToLastRow(index: Int, rowCount: Int, columnCount: Int): Boolean {
    return ceil((index + 1).toFloat() / columnCount).toInt() >= rowCount
}


@Preview(showDecoration = true)
@Composable
private fun VerticalGridPreview() {
    VerticalStaggeredGrid(
        columnCount = 4,
        columnSpacing = 4.dp,
        rowSpacing = 4.dp
    ) {
        (1..100).forEach { number ->
            Box(modifier = Modifier.fillMaxSize().background(Color.randomColor())) {
                Text(text = number.toString(), color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
