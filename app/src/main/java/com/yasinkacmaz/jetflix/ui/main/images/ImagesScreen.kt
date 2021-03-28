package com.yasinkacmaz.jetflix.ui.main.images

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.transform.BlurTransformation
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.navigationBarsPadding
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.widget.Pager
import com.yasinkacmaz.jetflix.ui.widget.PagerState
import com.yasinkacmaz.jetflix.util.transformation.SizeTransformation

@Composable
fun ImagesScreen(images: List<Image>) {
    if (images.isNotEmpty()) {
        val pagerState: PagerState = remember { PagerState(maxPage = (images.size - 1).coerceAtLeast(0)) }
        Pager(state = pagerState) {
            Image(images[page])
        }
    }
}

@Composable
private fun Image(image: Image) {
    val sizeTransformation = remember { SizeTransformation(percent = 50) }
    Box(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        CoilImage(
            data = image.url,
            contentDescription = stringResource(id = R.string.poster_content_description),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha = 0.85f),
            requestBuilder = { transformations(BlurTransformation(context = context, radius = 12f, sampling = 4f)) }
        )
        FloatingActionButton(
            onClick = {},
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = stringResource(id = R.string.likes_content_description),
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(text = image.voteCount.toString(), style = MaterialTheme.typography.body2)
            }
        }
        Card(
            modifier =Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
                .shadow(48.dp, clip = false)
                .zIndex(48f)
                .align(Alignment.Center),
            shape = RoundedCornerShape(12.dp), elevation = 24.dp
        ) {
            CoilImage(
                data = image.url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                requestBuilder = { transformations(sizeTransformation) },
                loading = {
                    CircularProgressIndicator(
                        Modifier
                            .size(120.dp)
                            .fillMaxHeight()
                    )
                }
            )
        }
    }
}
