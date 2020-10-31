package com.yasinkacmaz.jetflix.ui.main.images

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.transform.BlurTransformation
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.util.navigationBarsPadding
import com.yasinkacmaz.jetflix.ui.widget.Pager
import com.yasinkacmaz.jetflix.ui.widget.PagerState
import com.yasinkacmaz.jetflix.util.transformation.SizeTransformation
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ImagesScreen(images: List<Image>) {
    val pagerState: PagerState = run {
        val clock = AnimationClockAmbient.current
        remember(clock) { PagerState(clock, maxPage = (images.size - 1).coerceAtLeast(0)) }
    }
    Pager(state = pagerState) {
        Image(images[page])
    }
}

@Composable
private fun Image(image: Image) {
    val sizeTransformation = remember { SizeTransformation(percent = 50) }
    Box(modifier = Modifier.fillMaxSize()) {
        val context = ContextAmbient.current
        CoilImage(
            data = image.url,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize().drawLayer(alpha = 0.85f),
            requestBuilder = { transformations(BlurTransformation(context = context, radius = 12f, sampling = 4f)) }
        )
        FloatingActionButton(
            onClick = {},
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).navigationBarsPadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ThumbUp, modifier = Modifier.padding(end = 4.dp))
                Text(text = image.voteCount.toString(), style = MaterialTheme.typography.body2)
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 12.dp)
                .drawShadow(48.dp, clip = false).zIndex(48f).align(Alignment.Center),
            shape = RoundedCornerShape(12.dp), elevation = 24.dp
        ) {
            CoilImage(
                data = image.url,
                contentScale = ContentScale.FillWidth,
                requestBuilder = { transformations(sizeTransformation) },
                loading = { CircularProgressIndicator(Modifier.size(120.dp).fillMaxHeight()) },
            )
        }
    }
}
