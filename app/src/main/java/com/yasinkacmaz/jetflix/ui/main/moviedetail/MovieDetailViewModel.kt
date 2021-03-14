package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.ImageMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieService: MovieService,
    private val movieDetailMapper: MovieDetailMapper,
    private val creditsMapper: CreditsMapper,
    private val imageMapper: ImageMapper
) : ViewModel() {

    val uiState = MutableStateFlow(MovieDetailUiState())
    var uiValue
        get() = uiState.value
        set(value) {
            uiState.value = value
        }

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            uiValue = uiValue.copy(loading = true)
            try {
                val movieDetail = async {
                    val movieDetailResponse = movieService.fetchMovieDetail(movieId)
                    movieDetailMapper.map(movieDetailResponse)
                }
                val credits = async {
                    val creditsResponse = movieService.fetchMovieCredits(movieId)
                    creditsMapper.map(creditsResponse)
                }
                val images = async {
                    val imagesResponse = movieService.fetchMovieImages(movieId)
                    imageMapper.map(imagesResponse)
                }
                uiValue = uiValue.copy(
                    movieDetail = movieDetail.await(),
                    credits = credits.await(),
                    images = images.await(),
                    loading = false
                )
            } catch (exception: Exception) {
                uiValue = uiValue.copy(error = exception, loading = false)
            }
        }
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetail? = null,
        val credits: Credits = Credits(listOf(), listOf()),
        val images: List<Image> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
