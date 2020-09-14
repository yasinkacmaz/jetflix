package com.yasinkacmaz.playground.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.data.Genre
import com.yasinkacmaz.playground.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GenresViewModel @ViewModelInject constructor(private val movieService: MovieService) : ViewModel() {

    val uiState = MutableStateFlow(GenresUiState())
    private val uiValue get() = uiState.value

    fun fetchGenres() {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val genres = movieService.fetchGenres().genres.shuffled().take(5)
                delay(1000)
                uiState.value = uiValue.copy(genres = genres, fetchGenres = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, fetchGenres = false)
            }
            uiState.value = uiValue.copy(loading = false)
        }
    }

    data class GenresUiState(
        val fetchGenres: Boolean = true,
        val genres: List<Genre> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    ) {
        fun shouldFetchGenres() = fetchGenres && genres.isEmpty() && !loading
    }
}