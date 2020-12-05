package com.yasinkacmaz.jetflix.ui.main.moviedetail

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.parseJson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MovieDetailViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

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
        movieDetailViewModel = spyk(MovieDetailViewModel(movieService, movieDetailMapper, creditsMapper, imageMapper))
    }

    @Test
    fun `fetchMovieDetail success`() = coroutineTestRule.runBlockingTest {
        mockServices()

        movieDetailViewModel.fetchMovieDetail(movieId)

        verifyServices()
        verify {
            movieDetailViewModel.uiValue = MovieDetailViewModel.MovieDetailUiState(loading = true)
        }
        verify {
            movieDetailViewModel.uiValue = MovieDetailViewModel.MovieDetailUiState(
                movieDetailMapper.map(movieDetailResponse),
                creditsMapper.map(creditsResponse),
                imageMapper.map(imagesResponse),
                loading = false
            )
        }
    }

    @Test
    fun `fetchMovieDetail error`() = coroutineTestRule.runBlockingTest {
        GlobalScope.launch(Dispatchers.IO) {
            mockServices()
            val exception = IOException()
            coEvery { movieService.fetchMovieDetail(movieId) } throws exception

            movieDetailViewModel.fetchMovieDetail(movieId)

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
