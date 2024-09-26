package com.yasinkacmaz.jetflix.ui.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.error.ErrorRow
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheet
import com.yasinkacmaz.jetflix.ui.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.main.LocalDarkTheme
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieItem
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.settings.SettingsDialog
import com.yasinkacmaz.jetflix.ui.theme.spacing
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

private const val COLUMN_COUNT = 2

private val fullWidthSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(COLUMN_COUNT) }

@Composable
fun MoviesScreen(moviesViewModel: MoviesViewModel, filterViewModel: FilterViewModel) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val filterState = filterViewModel.filterState.collectAsState().value
    val searchQuery = moviesViewModel.searchQuery.collectAsState()
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = MaterialTheme.spacing.s),
            ) {
                JetflixAppBar(onSettingsClicked = { showSettingsDialog = true })
                JetflixSearchBar(searchQuery.value, moviesViewModel::onSearch)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = searchQuery.value.isBlank()) {
                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    content = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(id = R.string.title_filter_bottom_sheet),
                        )
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        content = { contentPadding ->
            MoviesGrid(contentPadding, moviesViewModel)
            if (showSettingsDialog) {
                SettingsDialog(onDialogDismissed = { showSettingsDialog = false })
            }
        },
    )

    if (openBottomSheet) {
        FilterBottomSheet(filterState, { openBottomSheet = false }, filterViewModel::onFilterStateChanged)
    }
}

@Composable
fun MoviesGrid(contentPadding: PaddingValues, moviesViewModel: MoviesViewModel) {
    val movies = moviesViewModel.moviesPagingData.collectAsLazyPagingItems()
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        merge(*moviesViewModel.stateChanges.toTypedArray())
            .onEach {
                gridState.scrollToItem(0)
                movies.refresh()
            }
            .launchIn(this)
    }

    val navController = LocalNavController.current
    val onMovieClicked: (Int) -> Unit = { movieId -> navController.navigate(Screen.MovieDetail(movieId)) }
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
        contentPadding = PaddingValues(
            bottom = contentPadding.calculateBottomPadding(),
            start = MaterialTheme.spacing.s,
            end = MaterialTheme.spacing.s,
        ),
        columns = GridCells.Fixed(COLUMN_COUNT),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s, Alignment.CenterVertically),
        state = gridState,
        content = {
            items(movies.itemCount) { index ->
                movies[index]?.let { MovieItem(it, Modifier.height(280.dp), onMovieClicked) }
            }

            when {
                movies.loadState.refresh is LoadState.Loading -> {
                    item(span = fullWidthSpan) {
                        LoadingColumn(title = stringResource(R.string.fetching_movies))
                    }
                }

                movies.loadState.append is LoadState.Loading -> {
                    item(span = fullWidthSpan) {
                        LoadingRow(title = stringResource(R.string.fetching_more_movies))
                    }
                }

                movies.loadState.refresh is LoadState.Error -> {
                    item(span = fullWidthSpan) {
                        ErrorColumn(message = (movies.loadState.refresh as LoadState.Error).error.message.orEmpty())
                    }
                }

                movies.loadState.append is LoadState.Error -> {
                    item(span = fullWidthSpan) {
                        ErrorRow(title = (movies.loadState.append as? LoadState.Error)?.error?.message.orEmpty())
                    }
                }
            }
        },
    )
}

@Composable
private fun JetflixAppBar(onSettingsClicked: () -> Unit) {
    var isDarkTheme by LocalDarkTheme.current
    val iconTint = animateColorAsState(
        if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
        label = "appIconTint",
    ).value
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onSettingsClicked) {
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.settings_content_description),
                tint = iconTint,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_jetflix),
            contentDescription = stringResource(id = R.string.app_name),
            tint = iconTint,
            modifier = Modifier.height(24.dp),
        )

        IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
            val contentDescriptionResId = if (isDarkTheme) {
                R.string.light_theme_content_description
            } else {
                R.string.dark_theme_content_description
            }
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.NightsStay else Icons.Default.WbSunny,
                contentDescription = stringResource(id = contentDescriptionResId),
                tint = iconTint,
            )
        }
    }
}

@Composable
private fun JetflixSearchBar(searchQuery: String, onSearch: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.s)
            .heightIn(max = 52.dp)
            .fillMaxWidth(),
        value = searchQuery,
        textStyle = MaterialTheme.typography.titleSmall,
        singleLine = true,
        shape = RoundedCornerShape(50),
        placeholder = { Text(stringResource(id = R.string.search_movies), color = Color.Gray) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.HighlightOff,
                    contentDescription = null,
                    modifier = Modifier.clickable { onSearch("") },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query -> onSearch(query) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}
