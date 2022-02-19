package com.yasinkacmaz.jetflix.ui.moviedetail.image

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.util.transformation.BlurTransformation
import com.yasinkacmaz.jetflix.util.transformation.SizeTransformation

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagesScreen(images: List<Image>, initialPage: Int) {
    if (images.isEmpty() || initialPage !in images.indices) return

    val pagerState = rememberPagerState(initialPage = initialPage)
    Box {
        HorizontalPager(state = pagerState, count = images.size) { page ->
            Image(images[page])
        }
        Index(position = pagerState.currentPage + 1, imageCount = pagerState.pageCount)
    }
}

@Composable
private fun Image(image: Image) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        BlurImage(image)
        Card(
            Modifier
                .padding(12.dp)
                .shadow(16.dp, RoundedCornerShape(12.dp))
                .animateContentSize()
        ) {
            Box {
                Poster(image)
                VoteCount(image.voteCount)
            }
        }
    }
}

@Composable
private fun BlurImage(image: Image) {
    val sampling = 4f
    val radius = 16f
    val context = LocalContext.current
    val (transformation, modifier) = remember {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            BlurTransformation(context = context, radius = radius, sampling = sampling) to Modifier
        } else {
            val downscalePercentage = 100 / sampling.toInt()
            // TODO: Modifier.blur works on Android 12 but you need to scroll tiny bit to it show
            SizeTransformation(downscalePercentage) to Modifier.blur(radius.dp)
        }
    }

    Image(
        painter = rememberImagePainter(image.url, builder = { transformations(transformation) }),
        contentDescription = stringResource(id = R.string.poster_content_description),
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .then(modifier)
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun BoxScope.Poster(image: Image) {
    val painter = rememberImagePainter(image.url)
    if (painter.state is ImagePainter.State.Loading) {
        CircularProgressIndicator(
            strokeWidth = 8.dp,
            modifier = Modifier
                .size(240.dp)
                .padding(32.dp)
        )
    } else {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.Companion
                .align(Alignment.Center)
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
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
                shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 12.dp)
            )
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(id = R.string.likes_content_description),
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(text = voteCount.toString(), style = MaterialTheme.typography.body2)
    }
}

@Composable
private fun BoxScope.Index(position: Int, imageCount: Int) {
    Text(
        text = "$position / $imageCount",
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(16.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colors.surface.copy(alpha = 0.3f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
