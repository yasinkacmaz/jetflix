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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.yasinkacmaz.jetflix.R

@Composable
fun ImagesScreen(images: List<Image>, initialPage: Int) {
    if (images.isEmpty() || initialPage !in images.indices) return

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f,
    ) { images.size }
    Box {
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
            Modifier
                .systemBarsPadding()
                .padding(12.dp)
                .shadow(16.dp, RoundedCornerShape(12.dp))
                .animateContentSize()
                .wrapContentSize(),
        ) {
            Box {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(image.url),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.FillWidth,
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
            .background(MaterialTheme.colors.surface)
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
                color = MaterialTheme.colors.surface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 12.dp),
            )
            .padding(4.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(id = R.string.likes_content_description),
            modifier = Modifier.padding(end = 4.dp),
        )
        Text(text = voteCount.toString(), style = MaterialTheme.typography.body2)
    }
}

@Composable
private fun BoxScope.Index(position: Int, imageCount: Int) {
    Text(
        text = "$position / $imageCount",
        style = MaterialTheme.typography.body2,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(4.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colors.surface.copy(alpha = 0.3f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}
