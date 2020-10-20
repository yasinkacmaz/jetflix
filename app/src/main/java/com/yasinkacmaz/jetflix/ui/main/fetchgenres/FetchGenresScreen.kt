package com.yasinkacmaz.jetflix.ui.main.fetchgenres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.genres.SelectedGenreAmbient
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen.Genres
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FetchGenresScreen() {
    val fetchGenresViewModel: FetchGenresViewModel = viewModel()
    val fetchGenresUiState = fetchGenresViewModel.uiState.collectAsState().value

    onActive {
        fetchGenresViewModel.fetchGenres()
    }

    when {
        fetchGenresUiState.loading -> {
            val context = ContextAmbient.current
            val title = context.getString(R.string.fetching_genres)
            LoadingColumn(title)
        }
        fetchGenresUiState.error != null -> {
            ErrorColumn(fetchGenresUiState.error.message.orEmpty())
        }
        fetchGenresUiState.genreUiModels.isNotEmpty() -> {
            SelectedGenreAmbient.current.value = fetchGenresUiState.genreUiModels.first()
            NavigatorAmbient.current.navigateTo(Genres(fetchGenresUiState.genreUiModels), keepCurrentScreen = false)
        }
    }
}
