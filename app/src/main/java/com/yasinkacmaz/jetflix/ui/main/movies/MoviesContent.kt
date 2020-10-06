package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MoviesContent(genre: Genre) {
    val moviesViewModel: MoviesViewModel = viewModel(key = genre.id.toString())
    val movieUiState = moviesViewModel.uiState.collectAsState().value

    onActive {
        moviesViewModel.fetchMovies(genre.id)
    }

    when {
        movieUiState.loading && movieUiState.movies.isEmpty() -> {
            val title = stringResource(id = R.string.fetching_movies, genre.name)
            LoadingColumn(title)
        }
        movieUiState.error != null && movieUiState.movies.isEmpty() -> {
            ErrorColumn(movieUiState.error.message.orEmpty())
        }
        movieUiState.movies.isNotEmpty() -> {
            LazyMovies(
                movieUiState.moviePairs,
                genre.name,
                movieUiState.loading,
                movieUiState.error
            ) {
                moviesViewModel.fetchMovies(genre.id)
            }
        }
    }
}

@Composable
private fun LazyMovies(
    movies: List<Pair<Movie, Movie>>,
    genreName: String,
    loading: Boolean,
    error: Throwable?,
    onLoadMore: () -> Unit
) {
    val navigator = NavigatorAmbient.current
    val onMovieClicked: (Int) -> Unit = { movieId ->
        navigator.navigateTo(MovieDetail(movieId))
    }
    LazyColumnForIndexed(items = movies) { index, moviePair ->
        MovieRow(moviePair, onMovieClicked)
        if (index == movies.lastIndex) {
            onActive {
                onLoadMore()
            }

            if (loading) {
                LoadingRow(title = stringResource(id = R.string.fetching_more_movies, genreName))
            }

            if (error != null) {
                LoadingRow(title = error.message!!)
            }
        }
    }
}

@Composable
private fun MovieRow(moviePair: Pair<Movie, Movie>, onMovieClicked: (Int) -> Unit = {}) {
    val padding = 8.dp
    Row(modifier = Modifier.padding(padding)) {
        val modifier = Modifier.weight(1f).preferredHeight(320.dp)
        MovieContent(moviePair.first, modifier, onMovieClicked)
        Spacer(modifier = Modifier.width(padding))
        MovieContent(moviePair.second, modifier, onMovieClicked)
    }
}

@Composable
@Preview
private fun MovieRowPreview() {
    Column {
        MovieRow(fakeMovie to fakeMovie)
    }
}

@Composable
@Preview
private fun MoviesPreview() {
    Column {
        MovieRow(fakeMovie to fakeMovie)
        MovieRow(fakeMovie to fakeMovie)
    }
}
