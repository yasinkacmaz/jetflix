package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen.Genres
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun FetchGenresContent() {
    val genresViewModel: GenresViewModel = viewModel()
    val genresUiState = genresViewModel.uiState.collectAsState().value

    onActive {
        genresViewModel.fetchGenres()
    }

    when {
        genresUiState.loading -> {
            val context = ContextAmbient.current
            val title = context.getString(R.string.fetching_genres)
            LoadingColumn(title)
        }
        genresUiState.error != null -> {
            ErrorColumn(genresUiState.error.message.orEmpty())
        }
        genresUiState.genres.isNotEmpty() -> {
            NavigatorAmbient.current.navigateTo(Genres(genresUiState.genres), keepCurrentScreen = false)
        }
    }
}
