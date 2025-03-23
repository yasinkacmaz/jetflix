package com.yasinkacmaz.jetflix.ui.movies.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.rateColor
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.movie_poster_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
    OutlinedCard(
        modifier = modifier,
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
        }
    }
}

@Composable
private fun BoxScope.MoviePoster(posterPath: String, movieName: String) {
    JetflixImage(
        data = posterPath,
        placeholder = rememberVectorPainter(Icons.Default.Movie),
        error = rememberVectorPainter(Icons.Filled.BrokenImage),
        contentDescription = stringResource(Res.string.movie_poster_content_description, movieName),
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
    )
}

@Composable
private fun MovieInfo(movie: Movie, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
        modifier = modifier.padding(MaterialTheme.spacing.s),
    ) {
        Text(
            text = movie.name,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            MovieFeature(icon = Icons.Default.DateRange, field = movie.releaseDate)
            MovieFeature(icon = Icons.Default.ThumbUp, field = movie.voteCount.toString())
            val rateColor = Color.rateColor(movieRate = movie.voteAverage)
            MovieFeature(
                Modifier
                    .background(rateColor, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.xs),
                Icons.Default.Star,
                movie.voteAverage.toString(),
            )
        }
    }
}

@Composable
private fun MovieFeature(modifier: Modifier = Modifier, icon: ImageVector, field: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
        Text(
            text = field,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(start = MaterialTheme.spacing.xxs),
        )
    }
}
