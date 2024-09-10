package com.yasinkacmaz.jetflix.ui.movies.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.theme.imageTint
import com.yasinkacmaz.jetflix.util.rateColors

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = { onMovieClicked(movie.id) },
    ) {
        Box {
            MoviePoster(movie.posterPath, movie.name)
            MovieInfo(
                movie,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0x97000000)),
            )
            MovieRate(
                movie.voteAverage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(2f)
                    .offset(y = 4.dp),
            )
        }
    }
}

@Composable
private fun BoxScope.MoviePoster(posterPath: String, movieName: String) {
    val painter = rememberAsyncImagePainter(
        model = posterPath,
        error = rememberVectorPainter(Icons.Filled.BrokenImage),
        placeholder = rememberVectorPainter(Icons.Default.Movie),
    )
    val colorFilter = when (painter.state) {
        is Loading, is Error -> ColorFilter.tint(MaterialTheme.colorScheme.imageTint())
        else -> null
    }
    val scale = if (painter.state !is AsyncImagePainter.State.Success) ContentScale.Fit else ContentScale.FillBounds

    Image(
        painter = painter,
        colorFilter = colorFilter,
        contentDescription = stringResource(id = R.string.movie_poster_content_description, movieName),
        contentScale = scale,
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
    )
}

@Composable
private fun MovieRate(rate: Double, modifier: Modifier) {
    val colors = Color.rateColors(movieRate = rate)
    val brush = remember(rate) { Brush.horizontalGradient(colors) }
    Text(
        text = rate.toString(),
        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
        modifier = modifier
            .background(brush, RoundedCornerShape(50))
            .padding(horizontal = 10.dp)
            .shadow(8.dp),
    )
}

@Composable
private fun MovieInfo(movie: Movie, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        MovieName(name = movie.name)
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            MovieFeature(Icons.Default.DateRange, movie.releaseDate)
            MovieFeature(Icons.Default.ThumbUp, movie.voteCount.toString())
        }
    }
}

@Composable
private fun MovieName(name: String) = Text(
    text = name,
    style = MaterialTheme.typography.titleMedium.copy(
        color = Color.White,
        letterSpacing = 1.5.sp,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W500,
    ),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)

@Composable
private fun MovieFeature(icon: ImageVector, field: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
        Text(
            text = field,
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color.White,
                letterSpacing = 1.5.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400,
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 2.dp),
        )
    }
}
