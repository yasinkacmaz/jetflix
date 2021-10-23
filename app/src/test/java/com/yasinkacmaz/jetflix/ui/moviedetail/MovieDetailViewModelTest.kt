package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.lifecycle.SavedStateHandle
import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.ui.navigation.ARG_MOVIE_ID
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.parseJson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.verifyOrder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    private val movieDetailMapper = MovieDetailMapper()
    private val creditsMapper = CreditsMapper()
    private val imageMapper = ImageMapper()

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    private val movieId = 111
    private val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")
    private val creditsResponse: CreditsResponse = parseJson("credits.json")
    private val imagesResponse: ImagesResponse = parseJson("images.json")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { savedStateHandle.get<String>(ARG_MOVIE_ID) } returns movieId.toString()
    }

    private fun initViewModel() {
        movieDetailViewModel =
            spyk(MovieDetailViewModel(savedStateHandle, movieService, movieDetailMapper, creditsMapper, imageMapper))
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `fetchMovieDetail success`() = runBlockingTest {
        GlobalScope.launch(Dispatchers.IO) {
            mockServices()
            initViewModel()

            verifyServices()
            verifyOrder {
                movieDetailViewModel.uiValue = MovieDetailViewModel.MovieDetailUiState(loading = true)
                movieDetailViewModel.uiValue = MovieDetailViewModel.MovieDetailUiState(
                    movieDetailMapper.map(movieDetailResponse),
                    creditsMapper.map(creditsResponse),
                    imageMapper.map(imagesResponse),
                    loading = false
                )
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `fetchMovieDetail error`() = coroutineTestRule.runBlockingTest {
        GlobalScope.launch(Dispatchers.IO) {
            mockServices()
            val exception = IOException()
            coEvery { movieService.fetchMovieDetail(movieId) } throws exception
            initViewModel()

            verifyServices()
            verifyOrder {
                movieDetailViewModel.uiValue = MovieDetailViewModel.MovieDetailUiState(loading = true)
                movieDetailViewModel.uiValue =
                    MovieDetailViewModel.MovieDetailUiState(loading = false, error = exception)
            }
        }
    }

    private fun mockServices() {
        coEvery { movieService.fetchMovieDetail(movieId) } returns movieDetailResponse
        coEvery { movieService.fetchMovieCredits(movieId) } returns creditsResponse
        coEvery { movieService.fetchMovieImages(movieId) } returns imagesResponse
    }

    private fun verifyServices() = coVerify {
        movieService.fetchMovieDetail(movieId)
        movieService.fetchMovieCredits(movieId)
        movieService.fetchMovieImages(movieId)
    }
}
