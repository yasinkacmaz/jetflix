package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.util.animation.ItemAnimationArgs
import com.yasinkacmaz.jetflix.util.animation.animateItem
import com.yasinkacmaz.jetflix.util.toDp
import kotlin.math.absoluteValue

@Composable
fun PeopleGridScreen(people: List<Person>) {
    val statusBarPadding = WindowInsets.statusBars.getTop(LocalDensity.current).toDp().dp
    val navigationBarPadding = WindowInsets.navigationBars.getBottom(LocalDensity.current).toDp().dp
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
        state = state,
        content = { peopleGridContent(people, columnCount, state) }
    )
}

private fun LazyGridScope.peopleGridContent(people: List<Person>, columnCount: Int, state: LazyGridState) {
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
            modifier = Modifier.graphicsLayer(
                alpha = animationData.alpha,
                scaleX = animationData.scale,
                scaleY = animationData.scale
            )
        )
    }
}

@Composable
private fun LazyGridState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val visibleItems = layoutInfo.visibleItemsInfo
    val scrollState = when {
        visibleItems.isEmpty() -> ScrollState.INITIAL
        isScrollingUp() -> ScrollState.UP
        else -> ScrollState.DOWN
    }
    val rowDelay = when (scrollState) {
        ScrollState.INITIAL -> index / columnCount
        ScrollState.UP -> (visibleItems.last().index - index).absoluteValue % (visibleItems.count() / columnCount)
        ScrollState.DOWN -> (index - visibleItems.first().index).absoluteValue % (visibleItems.count() / columnCount)
    } * 250
    val scrollDirectionMultiplier = if (scrollState == ScrollState.UP) 1 else -1
    val columnDelay = (columnCount - index % columnCount) * 150 * scrollDirectionMultiplier
    val easing = if (scrollState == ScrollState.UP) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}

enum class ScrollState { INITIAL, UP, DOWN }

// Taken from: https://stackoverflow.com/a/68731559
@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
