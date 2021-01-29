package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.genres.AmbientSelectedGenre
import com.yasinkacmaz.jetflix.util.modifier.gradientBackground
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun MovieContent(movie: Movie, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
    Box(modifier = modifier) {
        MovieRate(
            movie.voteAverage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f)
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 12.dp)
                .clickable(onClick = { onMovieClicked(movie.id) }),
            shape = RoundedCornerShape(size = 8.dp),
            elevation = 8.dp
        ) {
            Box {
                MoviePoster(movie.posterPath, movie.name)
                MovieInfo(
                    movie,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun MoviePoster(posterPath: String, movieName: String) {
    val backgroundColor = if (MaterialTheme.colors.isLight) Color.LightGray else MaterialTheme.colors.background
    val tint = if (MaterialTheme.colors.isLight) Color.DarkGray else MaterialTheme.colors.onBackground
    CoilImage(
        data = posterPath,
        contentDescription = stringResource(id = R.string.movie_poster_content_description, movieName),
        contentScale = ContentScale.None,
        modifier = Modifier.fillMaxSize(),
        loading = {
            Icon(
                imageVector = Icons.Default.Movie,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .background(color = backgroundColor)
                    .fillMaxSize()
            )
        }
    )
}

@Composable
private fun MovieRate(rate: Double, modifier: Modifier) {
    val shape = RoundedCornerShape(percent = 50)
    val selectedGenre = AmbientSelectedGenre.current.value
    Surface(
        shape = shape,
        elevation = 12.dp,
        modifier = modifier
    ) {
        Text(
            text = rate.toString(),
            style = MaterialTheme.typography.body1.copy(color = Color.White),
            modifier = Modifier
                .gradientBackground(listOf(selectedGenre.primaryColor, selectedGenre.secondaryColor), shape)
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
private fun MovieInfo(movie: Movie, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(Color(0x97000000))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
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
private fun MovieFeature(icon: ImageVector, field: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
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
