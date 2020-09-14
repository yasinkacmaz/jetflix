package com.yasinkacmaz.playground.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.data.Movie
import com.yasinkacmaz.playground.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel @ViewModelInject constructor(private val movieService: MovieService) : ViewModel() {

    val uiState = MutableStateFlow(MovieUiState())
    private val uiValue get() = uiState.value

    fun fetchMovies(genreId: Int) {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val movies = movieService.fetchMovies(genreId, 1).movies
                delay(1000)
                uiState.value = uiValue.copy(movies = movies, fetchMovies = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, fetchMovies = false)
            }
            uiState.value = uiValue.copy(loading = false)
        }
    }

    data class MovieUiState(
        val fetchMovies: Boolean = true,
        val movies: List<Movie> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    ) {
        fun shouldFetchMovies() = fetchMovies && movies.isEmpty() && !loading
    }
}
