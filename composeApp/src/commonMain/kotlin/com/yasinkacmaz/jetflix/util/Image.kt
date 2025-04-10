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
import coil3.compose.AsyncImage

@Composable
fun JetflixImage(
    modifier: Modifier,
    data: Any?,
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    contentDescription: String = "",
    contentScale: ContentScale = ContentScale.Fit,
) {
    var colorFilter: ColorFilter? by remember { mutableStateOf(ColorFilter.tint(Color.LightGray)) }
    var scale: ContentScale by remember { mutableStateOf(ContentScale.Fit) }
    AsyncImage(
        modifier = modifier,
        model = data,
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
