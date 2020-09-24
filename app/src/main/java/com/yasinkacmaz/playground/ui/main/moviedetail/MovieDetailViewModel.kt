package com.yasinkacmaz.playground.ui.main.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.data.MovieDetailResponse
import com.yasinkacmaz.playground.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModel @ViewModelInject constructor(private val movieService: MovieService) :
    ViewModel() {

    val uiState = MutableStateFlow(MovieDetailUiState())
    private val uiValue get() = uiState.value

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val movieDetail = movieService.fetchMovieDetail(movieId)
                uiState.value = uiValue.copy(movieDetail = movieDetail, loading = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetailResponse? = null,
        val loading: Boolean = false,
        val error: Throwable? = null
    ) {
        fun shouldFetchMovieDetail() = movieDetail == null && !loading
    }
}
