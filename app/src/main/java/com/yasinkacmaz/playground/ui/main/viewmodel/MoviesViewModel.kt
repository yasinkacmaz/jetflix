package com.yasinkacmaz.playground.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.data.Movie
import com.yasinkacmaz.playground.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalStdlibApi::class)
class MoviesViewModel @ViewModelInject constructor(private val movieService: MovieService) :
    ViewModel() {

    val uiState = MutableStateFlow(MovieUiState())
    private val uiValue get() = uiState.value

    fun fetchMovies(genreId: Int) {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val movies = buildList {
                    repeat(4) {
                        addAll(fetchMovies(genreId, it + 1))
                    }
                }.sortedByDescending(Movie::voteCount).toMoviePairs()
                delay(1000)
                uiState.value = uiValue.copy(movies = movies, fetchMovies = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, fetchMovies = false)
            }
            uiState.value = uiValue.copy(loading = false)
        }
    }

    private fun List<Movie>.toMoviePairs(): List<Pair<Movie, Movie>> = buildList {
        for (index in this@toMoviePairs.indices.step(2)) {
            add(this@toMoviePairs[index] to this@toMoviePairs[index + 1])
        }
    }

    private suspend fun fetchMovies(
        genreId: Int,
        page: Int
    ) = movieService.fetchMovies(genreId, page).movies

    data class MovieUiState(
        val fetchMovies: Boolean = true,
        val movies: List<Pair<Movie, Movie>> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    ) {
        fun shouldFetchMovies() = fetchMovies && movies.isEmpty() && !loading
    }
}
