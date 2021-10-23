package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.ui.navigation.ARG_MOVIE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    init {
        val movieId: Int = savedStateHandle.get<String>(ARG_MOVIE_ID)!!.toInt()
        fetchMovieDetail(movieId = movieId)
    }

    private fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            uiValue = uiValue.copy(loading = true, error = null)
            uiValue = try {
                val (movieDetail, credits, images) = fetchMovieInformation(movieId)
                uiValue.copy(movieDetail = movieDetail, credits = credits, images = images, loading = false)
            } catch (exception: Exception) {
                uiValue.copy(error = exception, loading = false)
            }
        }
    }

    private suspend fun fetchMovieInformation(movieId: Int) = coroutineScope {
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
        return@coroutineScope Triple(movieDetail.await(), credits.await(), images.await())
    }

    data class MovieDetailUiState(
        val movieDetail: MovieDetail? = null,
        val credits: Credits = Credits(listOf(), listOf()),
        val images: List<Image> = listOf(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
