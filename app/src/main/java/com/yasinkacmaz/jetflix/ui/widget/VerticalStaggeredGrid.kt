package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.util.animation.ScaleAndAlphaAnimation
import com.yasinkacmaz.jetflix.util.animation.ScaleAndAlphaArgs
import com.yasinkacmaz.jetflix.util.randomColor
import kotlin.math.ceil

// TODO: Can we compute items like LazyGrid to eliminate stutters.
@Composable
fun VerticalStaggeredGrid(
    modifier: Modifier = Modifier,
    itemCount: Int,
    columnCount: Int,
    columnSpacing: Dp = 0.dp,
    rowSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable (Int, Modifier) -> Unit
) {
    Layout(
        content = {
            for (index in 0..itemCount) {
                val scaleAndAlphaAnimation = remember(index) {
                    val animation =
                        tween<Float>(durationMillis = 500, delayMillis = 100 * index, easing = FastOutSlowInEasing)
                    val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                    ScaleAndAlphaAnimation(args = args, animation = animation)
                }
                val (scale, alpha) = scaleAndAlphaAnimation.scaleAndAlpha()
                itemContent(index, Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale))
            }
        },
        modifier = modifier.verticalScroll(rememberScrollState())
    ) { measurables, constraints ->
        val rowSpacingPx = rowSpacing.toIntPx()
        val columnSpacingPx = columnSpacing.toIntPx()
        val totalColumnSpacing = (columnCount - 1) * columnSpacingPx
        val horizontalPadding = contentPadding.start.toIntPx() + contentPadding.end.toIntPx()
        val rowCount = ceil(measurables.count() / columnCount.toFloat()).toInt()
        val itemWidth = (constraints.maxWidth - totalColumnSpacing - horizontalPadding) / columnCount
        val itemConstraints = constraints.copy(maxWidth = itemWidth)
        val columnHeights = IntArray(columnCount) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->
            val column = shortestColumnIndex(columnHeights)
            val placeable = measurable.measure(itemConstraints)
            columnHeights[column] += placeable.height
            columnHeights[column] += if (placingToLastRow(index, rowCount, columnCount)) 0 else rowSpacingPx
            placeable
        }

        val height = columnHeights.maxOrNull()!!.plus(contentPadding.top.toIntPx() + contentPadding.bottom.toIntPx())
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(constraints.maxWidth, height) {
            val columnY = IntArray(columnCount) { contentPadding.top.toIntPx() }
            placeables.forEachIndexed { index, placeable ->
                val columnIndex = shortestColumnIndex(columnY)
                val columnPadding = (columnIndex % columnCount) * columnSpacingPx
                val placeableX = (itemWidth * columnIndex) + columnPadding + contentPadding.start.toIntPx()
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

@Preview(showSystemUi = true)
@Composable
private fun VerticalGridPreview() {
    VerticalStaggeredGrid(
        itemCount = 180,
        columnCount = 4,
        columnSpacing = 4.dp,
        rowSpacing = 4.dp
    ) { index, modifier ->
        val height = remember(index) { (40..200).random().dp }
        val color = remember(index) { Color.randomColor() }
        Box(modifier = modifier.fillMaxSize().height(height).background(color)) {
            Text(text = index.toString(), color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}
