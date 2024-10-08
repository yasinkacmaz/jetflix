package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.animation.AnimationDuration
import com.yasinkacmaz.jetflix.util.toDp
import kotlin.math.absoluteValue

enum class ItemState { PLACING, PLACED }

@Composable
fun PeopleGridScreen(people: List<Person>) {
    val statusBarPadding = WindowInsets.statusBars.getTop(LocalDensity.current).toDp().dp
    val navigationBarPadding = WindowInsets.navigationBars.getBottom(LocalDensity.current).toDp().dp
    val horizontalPadding = MaterialTheme.spacing.xs
    val columnCount = 3
    val gridState = rememberLazyGridState()
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = statusBarPadding,
                bottom = navigationBarPadding,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
            horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
            state = gridState,
            content = { peopleGridContent(people, columnCount, gridState) },
        )
    }
}

private fun LazyGridScope.peopleGridContent(people: List<Person>, columnCount: Int, state: LazyGridState) {
    items(people.count()) { index ->
        val (delay, easing) = state.calculateDelayAndEasing(index, columnCount)
        val scale = animatePersonScale(delay, easing)
        Person(
            person = people[index],
            modifier = Modifier.scale(scale),
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
        ScrollState.UP -> (visibleItems.last().index - index).absoluteValue % (visibleItems.count())
        ScrollState.DOWN -> (index - visibleItems.first().index).absoluteValue % (visibleItems.count())
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
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
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

@Composable
private fun animatePersonScale(delay: Int = 0, easing: Easing): Float {
    val transitionState = remember {
        MutableTransitionState(ItemState.PLACING).apply { targetState = ItemState.PLACED }
    }
    val animationSpec =
        tween<Float>(durationMillis = AnimationDuration.SHORT.duration, delayMillis = delay, easing = easing)
    val label = "itemPlacement"
    val transition = rememberTransition(transitionState, label = label)

    val scale by transition.animateFloat(transitionSpec = { animationSpec }, label = "$label-Scale") { state ->
        when (state) {
            ItemState.PLACING -> 0f
            ItemState.PLACED -> 1f
        }
    }
    return scale
}
