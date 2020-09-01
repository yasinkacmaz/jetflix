package com.yasinkacmaz.playground.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.data.Movie
import com.yasinkacmaz.playground.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel @ViewModelInject constructor(private val movieService: MovieService) : ViewModel() {

    val uiState = MutableStateFlow(UiState())

    private val uiValue get() = uiState.value

    fun fetchMovies() {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val movies =
                    movieService.fetchPopularMovies(1).movies.map { it.copy(posterPath = "https://image.tmdb.org/t/p/w342${it.posterPath.orEmpty()}") }
                uiState.value = uiValue.copy(movies = movies)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception)
            }
            uiState.value = uiValue.copy(loading = false)
        }
    }

    data class UiState(
        val movies: List<Movie> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
