package com.yasinkacmaz.jetflix.ui.main.moviedetail.person

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.util.AmbientInsets
import com.yasinkacmaz.jetflix.util.animation.ScaleAndAlphaAnimation
import com.yasinkacmaz.jetflix.util.animation.ScaleAndAlphaArgs
import com.yasinkacmaz.jetflix.util.toDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PeopleGridScreen(people: List<Person>) {
    val insets = AmbientInsets.current
    val statusBarPadding = insets.statusBars.top.toDp().dp
    val navigationBarPadding = insets.navigationBars.bottom.toDp().dp
    val horizontalPadding = 4.dp
    val columnCount = 3
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(columnCount),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                end = horizontalPadding,
                top = statusBarPadding,
                bottom = navigationBarPadding
            ),
            content = {
                items(people.count()) { index ->
                    val person = people[index]
                    BoxWithConstraints(Modifier.padding(horizontal = horizontalPadding, vertical = 8.dp)) {
                        val scaleAndAlphaAnimation = remember(index) {
                            // TODO: Can we access visible item count to determine index more accurately for animation.
                            //  Lets say we have 500 items but only 10 of them is visible to user.
                            //  I want to get the index on the screen, not the index in the whole dataset.
                            val animation = tween<Float>(
                                durationMillis = 500,
                                delayMillis = 200 * (index % columnCount),
                                easing = FastOutSlowInEasing
                            )
                            val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                            ScaleAndAlphaAnimation(args = args, animation = animation)
                        }
                        val (scale, alpha) = scaleAndAlphaAnimation.scaleAndAlpha()
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
        )
    }
}
