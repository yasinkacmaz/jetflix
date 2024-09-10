package com.yasinkacmaz.jetflix.ui.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.error.ErrorRow
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieItem
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.util.toDp
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

private const val COLUMN_COUNT = 2
private val GRID_SPACING = 8.dp

private val fullWidthSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(COLUMN_COUNT) }

@Composable
fun MoviesGrid(modifier: Modifier, moviesViewModel: MoviesViewModel) {
    val movies = moviesViewModel.moviesPagingData.collectAsLazyPagingItems()
    val state = rememberLazyGridState()
    LaunchedEffect(Unit) {
        merge(moviesViewModel.filterStateChanges, moviesViewModel.searchQueryChanges)
            .onEach {
                state.scrollToItem(0)
                movies.refresh()
            }
            .launchIn(this)
    }

    val navController = LocalNavController.current
    val onMovieClicked: (Int) -> Unit = { movieId -> navController.navigate(Screen.MovieDetail(movieId)) }
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(COLUMN_COUNT),
        contentPadding = PaddingValues(
            start = GRID_SPACING,
            end = GRID_SPACING,
            top = GRID_SPACING,
            bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).toDp().dp.plus(GRID_SPACING),
        ),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACING, Alignment.CenterVertically),
        state = state,
        content = {
            items(movies.itemCount) { index ->
                val movie = movies[index]
                movie?.let {
                    MovieItem(it, Modifier.height(320.dp), onMovieClicked)
                }
            }

            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    item(span = fullWidthSpan) {
                        LoadingColumn(
                            title = stringResource(id = R.string.fetching_movies),
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                movies.loadState.append is LoadState.Loading -> {
                    item(span = fullWidthSpan) {
                        LoadingRow(
                            title = stringResource(R.string.fetching_more_movies),
                            modifier = Modifier.padding(vertical = GRID_SPACING),
                        )
                    }
                }

                movies.loadState.refresh is LoadState.Error -> {
                    val error = movies.loadState.refresh as LoadState.Error
                    item(span = fullWidthSpan) {
                        ErrorColumn(message = error.error.message.orEmpty(), modifier = Modifier.fillMaxSize())
                    }
                }

                movies.loadState.append is LoadState.Error -> {
                    item(span = fullWidthSpan) {
                        val message = (movies.loadState.append as? LoadState.Error)?.error?.message.orEmpty()
                        ErrorRow(title = message, modifier = Modifier.padding(vertical = GRID_SPACING))
                    }
                }
            }
        },
    )
}
