package com.yasinkacmaz.playground.ui.main.movie

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.playground.R.drawable
import com.yasinkacmaz.playground.R.string
import com.yasinkacmaz.playground.data.Genre
import com.yasinkacmaz.playground.data.Movie
import com.yasinkacmaz.playground.ui.main.MoviesViewModel
import com.yasinkacmaz.playground.ui.main.common.ErrorContent
import com.yasinkacmaz.playground.ui.main.common.Loading
import com.yasinkacmaz.playground.ui.widget.SpacedColumn
import com.yasinkacmaz.playground.util.toPosterUrl
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Movies(genre: Genre) {
    val moviesViewModel: MoviesViewModel = viewModel(key = genre.id.toString())
    val movieUiState = moviesViewModel.uiState.collectAsState().value
    if (movieUiState.shouldFetchMovies()) {
        moviesViewModel.fetchMovies(genre.id)
    }
    when {
        movieUiState.loading -> {
            val title = stringResource(id = string.fetching_movies, genre.name)
            Loading(title)
        }
        movieUiState.error != null -> {
            ErrorContent(movieUiState.error.message.orEmpty())
        }
        movieUiState.movies.isNotEmpty() -> {
            LazyMoviesGrid(movieUiState.movies)
        }
    }
}

@Composable
fun LazyMoviesGrid(movies: List<Pair<Movie, Movie>>) {
    LazyColumnFor(items = movies) { (firstMovie, secondMovie) ->
        MovieRow(firstMovie, secondMovie)
    }
}

@Composable
fun MovieRow(firstMovie: Movie, secondMovie: Movie) {
    Row(modifier = Modifier.padding(8.dp)) {
        val modifier = Modifier.weight(1f).preferredHeight(320.dp)
        MovieItem(firstMovie, modifier)
        Spacer(modifier = Modifier.width(8.dp))
        MovieItem(secondMovie, modifier)
    }
}

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier) {
    Stack(modifier = modifier) {
        MovieRate(movie.voteAverage, modifier = Modifier.gravity(Alignment.TopCenter))
        Card(
            modifier = Modifier.fillMaxSize().offset(y = 8.dp),
            shape = RoundedCornerShape(size = 8.dp),
            elevation = 8.dp
        ) {
            Stack {
                MoviePoster(movie.posterPath.orEmpty().toPosterUrl())
                MovieInfo(
                    movie,
                    modifier = Modifier.gravity(Alignment.BottomCenter).fillMaxWidth()
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
        modifier = Modifier.fillMaxSize().aspectRatio(0.75f),
        loading = {
            Icon(
                asset = vectorResource(id = drawable.ic_movie),
                tint = Color.DarkGray,
                modifier = Modifier.background(color = Color.LightGray).fillMaxSize()
            )
        }
    )
}

@Composable
fun MovieRate(rate: Double, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(255, 177, 10),
        elevation = 12.dp,
        modifier = modifier
    ) {
        Text(
            text = rate.toString(),
            style = TextStyle(fontSize = 16.sp),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp)
        )
    }
}

@Composable
fun MovieInfo(movie: Movie, modifier: Modifier) {
    Surface(color = Color(0x97000000), modifier = modifier) {
        SpacedColumn(
            spaceBetween = 8.dp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            MovieName(name = movie.name)
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                MovieFeature(drawable.ic_date, movie.firstAirDate)
                MovieFeature(drawable.ic_thumb_up, movie.voteCount.toString())
            }
        }
    }
}

@Composable
fun MovieName(name: String) {
    Text(
        text = name,
        style = TextStyle(
            color = Color.White,
            fontSize = 14.sp,
            letterSpacing = 4.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W500
        ),
        maxLines = 1
    )
}

@Composable
fun MovieFeature(@DrawableRes iconResId: Int, field: String) {
    Row(verticalGravity = Alignment.CenterVertically) {
        Image(
            asset = vectorResource(id = iconResId),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = field,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400
            ),
            overflow = Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
@Preview
fun MoviePreview() {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        MovieItem(fakeMovie)
    }
}

@Composable
@Preview
fun MovieRowPreview() {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        MovieRow(fakeMovie, fakeMovie)
    }
}
