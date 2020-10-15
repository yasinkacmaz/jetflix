package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModel @ViewModelInject constructor(
    private val movieService: MovieService,
    private val movieDetailMapper: MovieDetailMapper,
    private val creditsMapper: CreditsMapper
) : ViewModel() {

    val uiState = MutableStateFlow(MovieDetailUiState())
    private val uiValue get() = uiState.value

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            uiState.value = uiValue.copy(loading = true)
            try {
                val movieDetailResponse = movieService.fetchMovieDetail(movieId)
                val movieDetail = movieDetailMapper.map(movieDetailResponse)
                val creditsResponse = movieService.fetchMovieCredits(movieId)
                val credits = creditsMapper.map(creditsResponse)
                uiState.value = uiValue.copy(movieDetail = movieDetail, credits = credits, loading = false)
            } catch (exception: Exception) {
                uiState.value = uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetail? = null,
        val credits: Credits = Credits(listOf(), listOf()),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
