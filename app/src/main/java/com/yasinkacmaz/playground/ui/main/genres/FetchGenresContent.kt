package com.yasinkacmaz.playground.ui.main.genres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.viewModel
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.ui.main.common.ErrorContent
import com.yasinkacmaz.playground.ui.main.common.Loading
import com.yasinkacmaz.playground.ui.main.viewmodel.GenresViewModel
import com.yasinkacmaz.playground.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.playground.ui.navigation.Screen.GenresScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FetchGenresContent() {
    val genresViewModel: GenresViewModel = viewModel()
    val genresUiState = genresViewModel.uiState.collectAsState().value
    if (genresUiState.shouldFetchGenres()) {
        genresViewModel.fetchGenres()
    }
    when {
        genresUiState.loading -> {
            val context = ContextAmbient.current
            val title = context.getString(R.string.fetching_genres)
            Loading(title)
        }
        genresUiState.error != null -> {
            ErrorContent(genresUiState.error.message.orEmpty())
        }
        genresUiState.genres.isNotEmpty() -> {
            NavigatorAmbient.current.navigateTo(GenresScreen(genresUiState.genres), keepCurrentScreen = false)
        }
    }
}
