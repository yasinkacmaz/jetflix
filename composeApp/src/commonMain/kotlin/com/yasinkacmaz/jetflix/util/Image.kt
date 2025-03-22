package com.yasinkacmaz.jetflix.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.Transformation
import com.yasinkacmaz.jetflix.util.animation.AnimationDuration

@Composable
fun JetflixImage(
    modifier: Modifier,
    data: Any?,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Fit,
    transformations: List<Transformation> = emptyList(),
    crossfade: AnimationDuration? = null,
) {
    var colorFilter: ColorFilter? by remember { mutableStateOf(ColorFilter.tint(Color.LightGray)) }
    var scale: ContentScale by remember { mutableStateOf(ContentScale.Fit) }
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(data)
        .transformations(transformations)
        .apply { if (crossfade != null) crossfade(crossfade.duration) }
        .build()
    AsyncImage(
        modifier = modifier,
        model = imageRequest,
        colorFilter = colorFilter,
        contentScale = scale,
        placeholder = placeholder,
        error = error,
        onSuccess = {
            scale = contentScale
            colorFilter = null
        },
        contentDescription = contentDescription,
    )
}
