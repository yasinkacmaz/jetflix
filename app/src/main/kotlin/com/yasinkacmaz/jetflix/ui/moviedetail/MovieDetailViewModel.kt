package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: Int,
    private val movieService: MovieService,
    private val movieDetailMapper: MovieDetailMapper,
    private val creditsMapper: CreditsMapper,
    private val imageMapper: ImageMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    init {
        fetchMovieDetail(movieId = movieId)
    }

    private fun fetchMovieDetail(movieId: Int) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        _uiState.value = try {
            coroutineScope {
                val movieDetailResponse = async { movieService.fetchMovieDetail(movieId) }
                val creditsResponse = async { movieService.fetchMovieCredits(movieId) }
                val imagesResponse = async { movieService.fetchMovieImages(movieId) }
                _uiState.value.copy(
                    movieDetail = movieDetailMapper.map(movieDetailResponse.await()),
                    credits = creditsMapper.map(creditsResponse.await()),
                    images = imageMapper.map(imagesResponse.await()),
                    loading = false,
                )
            }
        } catch (exception: Exception) {
            _uiState.value.copy(error = exception, loading = false)
        }
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetail? = null,
        val credits: Credits = Credits(listOf(), listOf()),
        val images: List<Image> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null,
    )
}
