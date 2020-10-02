package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.Movie
import com.yasinkacmaz.jetflix.ui.main.common.ErrorContent
import com.yasinkacmaz.jetflix.ui.main.common.Loading
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.widget.SpacedColumn
import com.yasinkacmaz.jetflix.util.toPosterUrl
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MoviesContent(genre: Genre) {
    val moviesViewModel: MoviesViewModel = viewModel(key = genre.id.toString())
    val movieUiState = moviesViewModel.uiState.collectAsState().value
    if (movieUiState.shouldFetchMovies()) {
        moviesViewModel.fetchMovies(genre.id)
    }
    when {
        movieUiState.loading -> {
            val title = stringResource(id = R.string.fetching_movies, genre.name)
            Loading(title)
        }
        movieUiState.error != null -> {
            ErrorContent(movieUiState.error.message.orEmpty())
        }
        movieUiState.movies.isNotEmpty() -> {
            LazyMovies(movieUiState.movies)
        }
    }
}

@Composable
fun LazyMovies(movies: List<Pair<Movie, Movie>>) {
    val navigator = NavigatorAmbient.current
    val onMovieClicked: (Int) -> Unit = { movieId ->
        navigator.navigateTo(MovieDetailScreen(movieId))
    }
    LazyColumnFor(items = movies) { (firstMovie, secondMovie) ->
        MovieRow(firstMovie, secondMovie, onMovieClicked)
    }
}

@Composable
fun MovieRow(firstMovie: Movie, secondMovie: Movie, onMovieClicked: (Int) -> Unit = {}) {
    val padding = 8.dp
    Row(modifier = Modifier.padding(padding)) {
        val modifier = Modifier.weight(1f).preferredHeight(320.dp)
        MovieItem(firstMovie, modifier, onMovieClicked)
        Spacer(modifier = Modifier.width(padding))
        MovieItem(secondMovie, modifier, onMovieClicked)
    }
}

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier, onMovieClicked: (Int) -> Unit = {}) {
    Box(modifier = modifier) {
        MovieRate(movie.voteAverage, modifier = Modifier.align(Alignment.TopCenter))
        Card(
            modifier = Modifier.fillMaxSize().offset(y = 8.dp).clickable(onClick = {
                onMovieClicked(movie.id)
            }),
            shape = RoundedCornerShape(size = 8.dp),
            elevation = 8.dp
        ) {
            Box {
                MoviePoster(movie.posterPath.orEmpty().toPosterUrl())
                MovieInfo(
                    movie,
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
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
                asset = Icons.Default.Movie,
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
            spaceBetween = 4.dp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            MovieName(name = movie.name)
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                MovieFeature(Icons.Default.DateRange, movie.firstAirDate)
                MovieFeature(Icons.Default.ThumbUp, movie.voteCount.toString())
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
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.W500,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun MovieFeature(icon: VectorAsset, field: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(asset = icon, tint = Color.White, modifier = Modifier.size(14.dp))
        Text(
            text = field,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
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
