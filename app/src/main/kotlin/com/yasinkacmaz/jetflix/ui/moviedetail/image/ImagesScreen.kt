package com.yasinkacmaz.jetflix.ui.moviedetail.image

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.theme.spacing

@Composable
fun ImagesScreen(images: List<Image>, initialPage: Int) {
    if (images.isEmpty() || initialPage !in images.indices) return

    val pagerState = rememberPagerState(initialPage = initialPage, initialPageOffsetFraction = 0f) { images.size }
    Box(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, key = { images[it].url + it }, beyondViewportPageCount = 3) {
            Poster(images[it])
        }
        Index(position = pagerState.currentPage + 1, imageCount = pagerState.pageCount)
    }
}

@Composable
private fun Poster(image: Image) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        BlurImage(image.url)
        Card(
            modifier = Modifier
                .systemBarsPadding()
                .padding(MaterialTheme.spacing.m)
                .shadow(16.dp, RoundedCornerShape(12.dp))
                .wrapContentSize()
                .animateContentSize(),
        ) {
            Box {
                AsyncImage(
                    model = image.url,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
                VoteCount(image.voteCount)
            }
        }
    }
}

@Composable
private fun BlurImage(url: String) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(id = R.string.poster_content_description),
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .fillMaxSize()
            .blur(16.dp),
    )
}

@Composable
private fun BoxScope.VoteCount(voteCount: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.BottomStart)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 12.dp),
            )
            .padding(MaterialTheme.spacing.xs),
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(id = R.string.likes_content_description),
            modifier = Modifier.padding(end = MaterialTheme.spacing.xs),
        )
        Text(text = voteCount.toString(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun BoxScope.Index(position: Int, imageCount: Int) {
    Text(
        text = "$position / $imageCount",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(MaterialTheme.spacing.xs)
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            .padding(horizontal = MaterialTheme.spacing.s, vertical = MaterialTheme.spacing.xxs),
    )
}
