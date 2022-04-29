package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.util.animation.ItemAnimationArgs
import com.yasinkacmaz.jetflix.util.animation.animateItem
import com.yasinkacmaz.jetflix.util.toDp

@Composable
fun PeopleGridScreen(people: List<Person>) {
    val insets = LocalWindowInsets.current
    val statusBarPadding = insets.statusBars.top.toDp().dp
    val navigationBarPadding = insets.navigationBars.bottom.toDp().dp
    val horizontalPadding = 4.dp
    val columnCount = 3
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        modifier = Modifier.background(MaterialTheme.colors.surface),
        contentPadding = PaddingValues(
            start = horizontalPadding,
            end = horizontalPadding,
            top = statusBarPadding,
            bottom = navigationBarPadding
        ),
        state = state,
        content = { peopleGridContent(people, columnCount, state, horizontalPadding) }
    )
}

private fun LazyGridScope.peopleGridContent(
    people: List<Person>,
    columnCount: Int,
    state: LazyGridState,
    horizontalPadding: Dp
) {
    items(people.count()) { index ->
        val (delay, easing) = state.calculateDelayAndEasing(index, columnCount)
        val args = ItemAnimationArgs(
            scaleRange = 0f..1f,
            alphaRange = 0f..1f,
            delay = delay,
            easing = easing
        )
        val animationData = animateItem(args)
        Person(
            person = people[index],
            modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = 8.dp)
                .graphicsLayer(
                    alpha = animationData.alpha,
                    scaleX = animationData.scale,
                    scaleY = animationData.scale
                )
        )
    }
}

@Composable
private fun LazyGridState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val rowIndex = index / columnCount
    val visibleRows = layoutInfo.visibleItemsInfo.count()
    val firstVisibleRowIndex = firstVisibleItemIndex
    val firstVisibleRow = firstVisibleRowIndex + 1
    val scrollingToBottom = firstVisibleRowIndex <= rowIndex
    val isFirstLoad = visibleRows == 0
    val rowMultiplier = when {
        isFirstLoad -> rowIndex
        scrollingToBottom -> firstVisibleRowIndex + visibleRows - rowIndex
        else -> firstVisibleRow - rowIndex // scrolling to top
    }
    val rowDelay = 200 * rowMultiplier
    val scrollDirectionMultiplier = if (isFirstLoad || scrollingToBottom) 1 else -1
    val column = (index % columnCount) + 1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing = if (isFirstLoad || scrollingToBottom) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}
