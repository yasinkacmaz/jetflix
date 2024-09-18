package com.yasinkacmaz.jetflix.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.yasinkacmaz.jetflix.util.animation.AnimationDuration

private val colorRange = 0..256

fun Color.Companion.randomColor() = Color(colorRange.random(), colorRange.random(), colorRange.random())

@Composable
@Stable
fun Color.Companion.rateColor(movieRate: Double): Color = remember(movieRate) {
    when {
        movieRate <= 4.5 -> Color(0xff9c180e)
        movieRate < 7 -> Color(0xff963d09)
        movieRate < 8.5 -> Color(0xff578216)
        else -> Color(0xff0d750f)
    }
}

@Composable
fun GetVibrantColorFromPoster(posterUrl: String, color: Animatable<Color, AnimationVector4D>) {
    val context = LocalContext.current
    LaunchedEffect(posterUrl) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(posterUrl)
            .size(128)
            .allowHardware(false)
            .build()

        val bitmap = (loader.execute(request) as? SuccessResult)?.drawable?.toBitmap() ?: return@LaunchedEffect
        val vibrantColor = Palette.from(bitmap).generate().getVibrantColor(color.value.toArgb())
        color.animateTo(Color(vibrantColor), tween(AnimationDuration.LONG.duration))
    }
}
