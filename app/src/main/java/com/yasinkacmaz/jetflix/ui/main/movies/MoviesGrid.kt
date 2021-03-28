package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.insets.LocalWindowInsets
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.error.ErrorRow
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.main.movie.Movie
import com.yasinkacmaz.jetflix.ui.main.movie.MovieContent
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetail
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun MoviesGrid() {
    val moviesViewModel = viewModel<MoviesViewModel>()
    val movies = moviesViewModel.movies.collectAsLazyPagingItems()
    val state = rememberLazyListState()
    val filterStateChanges = moviesViewModel.filterStateChanges
    filterStateChanges
        .onEach {
            state.scrollToItem(0)
            movies.refresh()
        }
        .launchIn(rememberCoroutineScope())
    LazyMoviesGrid(movies, state)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyMoviesGrid(
    moviePagingItems: LazyPagingItems<Movie>,
    state: LazyListState
) {
    val navigator = LocalNavigator.current
    val onMovieClicked: (Int) -> Unit = { movieId ->
        navigator.navigateTo(MovieDetail(movieId))
    }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 8.dp, bottom = LocalWindowInsets.current.navigationBars.top.dp),
        state = state,
        content = {
            items(moviePagingItems.itemCount) { index ->
                val movie = moviePagingItems[index] ?: return@items
                val modifier = Modifier
                    .padding(end = 8.dp)
                    .padding(vertical = 8.dp)
                BoxWithConstraints(modifier) {
                    MovieContent(movie, Modifier.height(320.dp), onMovieClicked)
                }
            }

            // TODO: LazyVerticalGrid does not have span strategy.
            //  Find a way to display loading and error items at full width or size.
            renderLoading(moviePagingItems.loadState)
            renderError(moviePagingItems.loadState)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderLoading(loadState: CombinedLoadStates) {
    when {
        loadState.refresh is LoadState.Loading -> {
            item {
                val title = stringResource(id = R.string.fetching_movies)
                // TODO: Find a way to fill max size
                LoadingColumn(title)
            }
        }
        loadState.append is LoadState.Loading -> {
            item {
                val title = stringResource(R.string.fetching_more_movies)
                // TODO: Find a way to fill max width
                LoadingRow(title = title)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridScope.renderError(loadState: CombinedLoadStates) {
    when {
        loadState.refresh is LoadState.Error -> {
            val error = loadState.refresh as LoadState.Error
            item {
                // TODO: Find a way to fill max size
                ErrorColumn(error.error.message.orEmpty())
            }
        }
        loadState.append is LoadState.Error -> {
            val error = loadState.append as LoadState.Error
            item {
                // TODO: Find a way to fill max width
                ErrorRow(title = error.error.message.orEmpty())
            }
        }
    }
}
