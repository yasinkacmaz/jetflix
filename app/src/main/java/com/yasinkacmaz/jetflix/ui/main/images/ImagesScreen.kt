package com.yasinkacmaz.jetflix.ui.main.images

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.transform.BlurTransformation
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.util.InsetsAmbient
import com.yasinkacmaz.jetflix.util.navigationBarsPadding
import com.yasinkacmaz.jetflix.util.toDp
import com.yasinkacmaz.jetflix.util.transformation.SizeTransformation
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ImagesScreen(images: List<Image>) {
    val configuration = ConfigurationAmbient.current
    val width = configuration.screenWidthDp.dp
    val statusBars = InsetsAmbient.current.statusBars
    val navigationBars = InsetsAmbient.current.navigationBars
    val height = configuration.screenHeightDp.dp + statusBars.top.toDp().dp + navigationBars.bottom.toDp().dp
    val sizeTransformation = SizeTransformation(50)
    LazyRowFor(
        items = images,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface)
    ) { image ->
        Image(image, width, height, sizeTransformation)
    }
}

@Composable
private fun Image(image: Image, width: Dp, height: Dp, sizeTransformation: SizeTransformation) {
    Box(modifier = Modifier.size(width, height)) {
        val context = ContextAmbient.current
        CoilImage(
            data = image.url,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.size(width, height).drawLayer(alpha = 0.85f),
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
            modifier = Modifier.width(width).wrapContentHeight().padding(horizontal = 12.dp)
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
