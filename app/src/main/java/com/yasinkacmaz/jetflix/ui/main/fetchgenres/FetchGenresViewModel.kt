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
    var uiValue
        get() = uiState.value
        set(value) {
            uiState.value = value
        }

    fun fetchGenres() {
        viewModelScope.launch {
            uiValue = uiValue.copy(loading = true)
            uiValue = try {
                val genres = movieService.fetchGenres().genres.map(genreUiModelMapper::map)
                uiValue.copy(genreUiModels = genres, loading = false)
            } catch (exception: Exception) {
                uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class FetchGenresUiState(
        val genreUiModels: List<GenreUiModel> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
