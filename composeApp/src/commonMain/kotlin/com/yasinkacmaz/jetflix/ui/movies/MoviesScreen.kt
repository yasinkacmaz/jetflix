package com.yasinkacmaz.jetflix.ui.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalDarkTheme
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.common.Error
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheet
import com.yasinkacmaz.jetflix.ui.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieItem
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.settings.SettingsDialog
import com.yasinkacmaz.jetflix.ui.theme.spacing
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.app_name
import jetflix.composeapp.generated.resources.dark_theme_content_description
import jetflix.composeapp.generated.resources.favorites
import jetflix.composeapp.generated.resources.fetching_more_movies
import jetflix.composeapp.generated.resources.fetching_movies
import jetflix.composeapp.generated.resources.ic_jetflix
import jetflix.composeapp.generated.resources.light_theme_content_description
import jetflix.composeapp.generated.resources.search_movies
import jetflix.composeapp.generated.resources.settings_content_description
import jetflix.composeapp.generated.resources.title_filter_bottom_sheet
import jetflix.composeapp.generated.resources.unable_to_fetch_movies
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val COLUMN_COUNT = 2

private val fullWidthSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(COLUMN_COUNT) }

@Composable
fun MoviesScreen(moviesViewModel: MoviesViewModel, filterViewModel: FilterViewModel) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val filterState = filterViewModel.filterState.collectAsState().value
    val searchQuery = moviesViewModel.searchQuery.collectAsState()
    val movies = moviesViewModel.movies.collectAsState().value
    val pagingState = moviesViewModel.pagingState.collectAsState().value

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
                            contentDescription = stringResource(Res.string.title_filter_bottom_sheet),
                        )
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        content = { contentPadding ->
            val gridState = rememberLazyGridState()
            LaunchedEffect(Unit) {
                merge(*moviesViewModel.stateChanges.toTypedArray())
                    .onEach {
                        gridState.scrollToItem(0)
                        moviesViewModel.refresh()
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
                    items(movies) { movie ->
                        MovieItem(movie, Modifier.height(280.dp), onMovieClicked)
                    }

                    when (pagingState) {
                        is MoviesViewModel.PagingState.Loading -> {
                            item(span = fullWidthSpan) {
                                val message = if (movies.isEmpty()) {
                                    Res.string.fetching_movies
                                } else {
                                    Res.string.fetching_more_movies
                                }
                                Loading(
                                    modifier = if (movies.isEmpty()) {
                                        Modifier.fillMaxSize()
                                    } else {
                                        Modifier.fillMaxWidth()
                                    },
                                    title = stringResource(message),
                                )
                            }
                        }

                        is MoviesViewModel.PagingState.Error -> {
                            item(span = fullWidthSpan) {
                                val modifier = if (movies.isEmpty()) {
                                    Modifier.fillMaxSize()
                                } else {
                                    Modifier
                                }
                                Error(
                                    modifier = modifier,
                                    message = stringResource(Res.string.unable_to_fetch_movies),
                                    onRetryClicked = moviesViewModel::retry,
                                )
                            }
                        }

                        else -> {}
                    }
                },
            )

            val shouldLoadMore = remember(movies.size) {
                derivedStateOf {
                    val layoutInfo = gridState.layoutInfo
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                    movies.isNotEmpty() &&
                            lastVisibleItemIndex >= movies.size - 5 &&
                            pagingState !is MoviesViewModel.PagingState.Loading &&
                            pagingState !is MoviesViewModel.PagingState.Error
                }
            }

            LaunchedEffect(shouldLoadMore.value) {
                if (shouldLoadMore.value) {
                    moviesViewModel.loadMore()
                }
            }

            if (showSettingsDialog) {
                SettingsDialog(onDialogDismissed = { showSettingsDialog = false })
            }
        },
    )

    if (openBottomSheet) {
        FilterBottomSheet(filterState, { openBottomSheet = false }, filterViewModel::onFilterStateChanged)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JetflixAppBar(onSettingsClicked: () -> Unit) {
    var isDarkTheme by LocalDarkTheme.current
    val navController = LocalNavController.current
    val iconTint = animateColorAsState(
        if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
        label = "appIconTint",
    ).value
    TopAppBar(
        title = {
            Icon(
                painter = painterResource(Res.drawable.ic_jetflix),
                contentDescription = stringResource(Res.string.app_name),
                tint = iconTint,
                modifier = Modifier.height(24.dp),
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Favorites) }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = stringResource(Res.string.favorites),
                    tint = iconTint,
                )
            }

            IconButton(onClick = onSettingsClicked) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(Res.string.settings_content_description),
                    tint = iconTint,
                )
            }

            IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                val contentDescriptionResource = if (isDarkTheme) {
                    Res.string.light_theme_content_description
                } else {
                    Res.string.dark_theme_content_description
                }
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.NightsStay else Icons.Default.WbSunny,
                    contentDescription = stringResource(contentDescriptionResource),
                    tint = iconTint,
                )
            }
        },
    )
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
        placeholder = { Text(stringResource(Res.string.search_movies), color = Color.Gray) },
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
