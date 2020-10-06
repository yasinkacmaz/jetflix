package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MovieContent(movie: Movie, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
    Box(modifier = modifier) {
        MovieRate(movie.voteAverage, modifier = Modifier.align(Alignment.TopCenter))
        Card(
            modifier = Modifier.fillMaxSize().offset(y = 8.dp).clickable(onClick = { onMovieClicked(movie.id) }),
            shape = RoundedCornerShape(size = 8.dp),
            elevation = 8.dp
        ) {
            Box {
                MoviePoster(movie.posterPath)
                MovieInfo(movie, modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth())
            }
        }
    }
}

@Composable
private fun MoviePoster(posterPath: String) {
    CoilImage(
        data = posterPath,
        contentScale = ContentScale.None,
        modifier = Modifier.fillMaxSize(),
        loading = {
            Icon(
                asset = Icons.Default.Movie,
                tint = Color.DarkGray,
                modifier = Modifier.background(color = Color.LightGray).fillMaxSize()
            )
        }
    )
}

@Composable
private fun MovieRate(rate: Double, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(255, 177, 10),
        elevation = 12.dp,
        modifier = modifier
    ) {
        Text(
            text = rate.toString(),
            style = MaterialTheme.typography.body2.copy(color = Color.White),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun MovieInfo(movie: Movie, modifier: Modifier) {
    Column(modifier = modifier.background(Color(0x97000000)).padding(horizontal = 8.dp, vertical = 4.dp)) {
        MovieName(name = movie.name)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            MovieFeature(Icons.Default.DateRange, movie.releaseDate)
            MovieFeature(Icons.Default.ThumbUp, movie.voteCount.toString())
        }
    }
}

@Composable
private fun MovieName(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.subtitle1.copy(
            color = Color.White,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W500,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun MovieFeature(icon: VectorAsset, field: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(asset = icon, tint = Color.White, modifier = Modifier.size(14.dp))
        Text(
            text = field,
            style = MaterialTheme.typography.subtitle2.copy(
                color = Color.White,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
@Preview
private fun MoviePreview() {
    MovieContent(fakeMovie)
}
