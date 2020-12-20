package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.error.ErrorRow
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.navigation.AmbientNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetail
import com.yasinkacmaz.jetflix.util.AmbientInsets
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MoviesContent(genre: Genre) {
    val moviesViewModel: MoviesViewModel = viewModel(key = genre.id.toString())
    val movieUiState = moviesViewModel.uiState.collectAsState().value

    onActive {
        if (movieUiState.movies.isEmpty()) {
            moviesViewModel.fetchMovies(genre.id)
        }
    }

    when {
        movieUiState.loading && movieUiState.movies.isEmpty() -> {
            val title = stringResource(id = R.string.fetching_movies, genre.name.orEmpty())
            LoadingColumn(title)
        }
        movieUiState.error != null && movieUiState.movies.isEmpty() -> {
            ErrorColumn(movieUiState.error.message.orEmpty())
        }
        movieUiState.movies.isNotEmpty() -> {
            LazyMoviesGrid(movieUiState, genre) {
                moviesViewModel.fetchMovies(genre.id)
            }
        }
    }
}

// TODO: LazyVerticalGrid does not have span strategy.
//  Find a way to display loading item at full width.
//  Find a way to add padding between items without modifying all items.
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyMoviesGrid(
    uiState: MoviesViewModel.MovieUiState,
    genre: Genre,
    onLoadMore: () -> Unit
) {
    val navigator = AmbientNavigator.current
    val onMovieClicked: (Int) -> Unit = { movieId ->
        navigator.navigateTo(MovieDetail(movieId))
    }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 8.dp, bottom = AmbientInsets.current.navigationBars.top.dp),
        content = {
            itemsIndexed(
                uiState.movies,
                itemContent = { index, movie ->
                    WithConstraints(Modifier.padding(end = 8.dp).padding(vertical = 8.dp)) {
                        MovieContent(movie, Modifier.preferredHeight(320.dp), onMovieClicked)
                    }
                    val lastIndex = index == uiState.movies.lastIndex
                    LazyMovieGridPagingContent(lastIndex, onLoadMore, uiState.loading, genre, uiState.error)
                }
            )
        }
    )
}

@Composable
private fun LazyMovieGridPagingContent(
    lastIndex: Boolean,
    onLoadMore: () -> Unit,
    loading: Boolean,
    genre: Genre,
    error: Throwable?
) {

    if (lastIndex) {
        onActive {
            onLoadMore()
        }

        if (loading) {
            val title = stringResource(R.string.fetching_more_movies, genre.name.orEmpty())
            LoadingRow(title = title)
        }

        if (error != null) {
            ErrorRow(title = error.message!!)
        }
    }
}

@Composable
@Preview
private fun MoviesGridPreview() {
    LazyMoviesGrid(MoviesViewModel.MovieUiState(listOf(fakeMovie, fakeMovie, fakeMovie)), Genre(1, "Genre")) {}
}
