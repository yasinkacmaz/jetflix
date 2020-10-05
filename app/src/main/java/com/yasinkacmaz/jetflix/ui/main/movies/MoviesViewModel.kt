package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.util.toPairs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel @ViewModelInject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) : ViewModel() {

    val uiState = MutableStateFlow(MovieUiState())
    private val uiValue get() = uiState.value

    fun fetchMovies(genreId: Int) {
        if (uiValue.shouldFetchMovies().not()) return
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val moviesResponse = movieService.fetchMovies(genreId, uiValue.page)
                val movies = uiValue.movies.apply {
                    addAll(movieMapper.map(moviesResponse.movies))
                    sortedByDescending(Movie::voteCount)
                }
                val page = if (uiValue.page >= moviesResponse.totalPages) {
                    PAGE_INVALID
                } else {
                    uiValue.page + 1
                }
                uiState.value = uiValue.copy(movies = movies, page = page, loading = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class MovieUiState(
        val movies: MutableList<Movie> = mutableListOf(),
        val loading: Boolean = false,
        val error: Throwable? = null,
        val page: Int = 1
    ) {
        fun shouldFetchMovies() = !loading && page != PAGE_INVALID
        val moviePairs get() = movies.toPairs()
    }

    companion object {
        private const val PAGE_INVALID = -1
    }
}
