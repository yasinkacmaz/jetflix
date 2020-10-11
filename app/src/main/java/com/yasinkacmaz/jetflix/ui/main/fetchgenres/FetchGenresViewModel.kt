package com.yasinkacmaz.jetflix.ui.main.fetchgenres

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FetchGenresViewModel @ViewModelInject constructor(
    private val movieService: MovieService,
    private val genreUiModelMapper: GenreUiModelMapper
) : ViewModel() {

    val uiState = MutableStateFlow(FetchGenresUiState())
    private val uiValue get() = uiState.value

    fun fetchGenres() {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val genres = movieService.fetchGenres().genres.map(genreUiModelMapper::map)
                uiState.value = uiValue.copy(genreUiModels = genres, loading = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class FetchGenresUiState(
        val genreUiModels: List<GenreUiModel> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
