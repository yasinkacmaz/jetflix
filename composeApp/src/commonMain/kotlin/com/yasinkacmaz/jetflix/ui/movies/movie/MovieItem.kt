package com.yasinkacmaz.jetflix.ui.movies.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.rateColor
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.broken_image
import jetflix.composeapp.generated.resources.date_range
import jetflix.composeapp.generated.resources.movie
import jetflix.composeapp.generated.resources.movie_poster_content_description
import jetflix.composeapp.generated.resources.star
import jetflix.composeapp.generated.resources.thumb_up
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LazyGridItemScope.MovieItem(movie: Movie, onMovieClicked: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier.animateItem().aspectRatio(2 / 3f),
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
        placeholder = painterResource(Res.drawable.movie),
        error = painterResource(Res.drawable.broken_image),
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
            MovieFeature(icon = painterResource(Res.drawable.date_range), field = movie.releaseDate)
            MovieFeature(icon = painterResource(Res.drawable.thumb_up), field = movie.voteCount.toString())
            val rateColor = Color.rateColor(movieRate = movie.voteAverage)
            MovieFeature(
                Modifier
                    .background(rateColor, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.xs),
                painterResource(Res.drawable.star),
                movie.voteAverage.toString(),
            )
        }
    }
}

@Composable
private fun MovieFeature(modifier: Modifier = Modifier, icon: Painter, field: String) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
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
