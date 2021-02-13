package com.yasinkacmaz.jetflix.ui.main.fetchgenres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.genres.LocalSelectedGenre
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen.Genres
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FetchGenresScreen() {
    val fetchGenresViewModel: FetchGenresViewModel = viewModel()
    val fetchGenresUiState = fetchGenresViewModel.uiState.collectAsState().value

    DisposableEffect(Unit) {
        fetchGenresViewModel.fetchGenres()
        onDispose {
        }
    }

    when {
        fetchGenresUiState.loading -> {
            val context = LocalContext.current
            val title = context.getString(R.string.fetching_genres)
            LoadingColumn(title)
        }
        fetchGenresUiState.error != null -> {
            ErrorColumn(fetchGenresUiState.error.message.orEmpty())
        }
        fetchGenresUiState.genreUiModels.isNotEmpty() -> {
            LocalSelectedGenre.current.value = fetchGenresUiState.genreUiModels.first()
            LocalNavigator.current.navigateTo(Genres(fetchGenresUiState.genreUiModels), keepCurrentScreen = false)
        }
    }
}
