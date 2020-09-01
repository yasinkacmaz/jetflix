package com.yasinkacmaz.playground.ui.main

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.data.Movie
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MovieLayout(movie: Movie) {
    Stack(modifier = Modifier.width(160.dp).height(213.dp)) {
        MovieRate(movie.voteAverage, modifier = Modifier.gravity(Companion.TopCenter))
        Card(modifier = Modifier.width(160.dp).height(213.dp).offset(y = 8.dp), elevation = 4.dp) {
            Stack {
                MoviePoster(movie.posterPath.orEmpty())
                MovieName(
                    name = movie.name,
                    modifier = Modifier.gravity(Alignment.BottomStart).fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun MoviePoster(posterPath: String) {
    CoilImage(
        data = posterPath,
        contentScale = ContentScale.None,
        modifier = Modifier.fillMaxSize(),
        loading = {
            Icon(
                asset = vectorResource(id = R.drawable.ic_movie),
                tint = Color.DarkGray,
                modifier = Modifier.background(color = Color.LightGray).fillMaxSize()
            )
        }
    )
}

@Composable
fun MovieRate(rate: Double, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(255, 177, 10),
        elevation = 8.dp,
        modifier = modifier
    ) {
        Text(
            text = rate.toString(),
            style = TextStyle(fontSize = 12.sp),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
        )
    }
}

@Composable
fun MovieName(name: String, modifier: Modifier) {
    Surface(color = Color(0x66000000), modifier = modifier) {
        Text(
            text = name,
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = 3.sp,
                fontFamily = FontFamily.SansSerif
            ),
            overflow = TextOverflow.Clip,
            maxLines = 1
        )
    }
}

@Composable
@Preview
fun moviePreview() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        MovieLayout(
            Movie(
                "19.07.1997",
                "One Piece",
                "Japanese",
                "A pirate anime",
                "Image Url",
                9.24,
                1337
            )
        )
    }
}