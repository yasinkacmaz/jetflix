@file:OptIn(ExperimentalFoundationApi::class)

package com.yasinkacmaz.jetflix.ui.main.moviedetail.person

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.util.animation.ScaleAndAlphaArgs
import com.yasinkacmaz.jetflix.util.animation.scaleAndAlpha
import com.yasinkacmaz.jetflix.util.toDp
import dev.chrisbanes.accompanist.insets.LocalWindowInsets

@Composable
fun PeopleGridScreen(people: List<Person>) {
    val insets = LocalWindowInsets.current
    val statusBarPadding = insets.statusBars.top.toDp().dp
    val navigationBarPadding = insets.navigationBars.bottom.toDp().dp
    val horizontalPadding = 4.dp
    val columnCount = 3
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)
    ) {
        val state = rememberLazyListState()
        LazyVerticalGrid(
            cells = GridCells.Fixed(columnCount),
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
}

private fun LazyGridScope.peopleGridContent(
    people: List<Person>,
    columnCount: Int,
    state: LazyListState,
    horizontalPadding: Dp
) {
    items(people.count()) { index ->
        val (delay, easing) = state.calculateDelayAndEasing(index, columnCount)
        BoxWithConstraints(Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)) {
            val animation = tween<Float>(durationMillis = 500, delayMillis = delay, easing = easing)
            val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
            val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
            val person = people[index]
            Person(
                profilePhotoUrl = person.profilePhotoUrl,
                name = person.name,
                job = person.character,
                gender = person.gender,
                modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale)
            )
        }
    }
}

@Composable
private fun LazyListState.calculateDelayAndEasing(index: Int, columnCount: Int): Pair<Int, Easing> {
    val row = index / columnCount
    val column = index % columnCount
    val firstVisibleRow = firstVisibleItemIndex + 1
    val visibleRows = layoutInfo.visibleItemsInfo.count()
    val scrollingToBottom = firstVisibleRow < row
    val rowDelay = 200 * when {
        scrollingToBottom && visibleRows != 0 -> visibleRows + firstVisibleRow - row // scrolling to bottom
        visibleRows == 0 -> row // initial load
        else -> 1 // scrolling to top
    }
    val scrollDirectionMultiplier = if (scrollingToBottom || visibleRows == 0) 1 else -1
    val columnDelay = column * 150 * scrollDirectionMultiplier
    val easing = if (scrollingToBottom || visibleRows == 0) LinearOutSlowInEasing else FastOutSlowInEasing
    return rowDelay + columnDelay to easing
}
