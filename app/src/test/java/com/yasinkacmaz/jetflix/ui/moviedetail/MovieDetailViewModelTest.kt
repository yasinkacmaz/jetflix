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
import com.yasinkacmaz.jetflix.util.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
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

    private val movieId = 1337
    private val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")
    private val creditsResponse: CreditsResponse = parseJson("credits.json")
    private val imagesResponse: ImagesResponse = parseJson("images.json")

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { savedStateHandle.get<String>(ARG_MOVIE_ID) } returns movieId.toString()
        with(movieService) {
            coEvery { fetchMovieDetail(movieId) } returns movieDetailResponse
            coEvery { fetchMovieCredits(movieId) } returns creditsResponse
            coEvery { fetchMovieImages(movieId) } returns imagesResponse
        }
    }

    @Test
    fun `fetchMovieDetail success`() = runTest {
        movieDetailViewModel = createViewModel()

        val stateValues = movieDetailViewModel.uiState.test()

        coVerify {
            movieService.fetchMovieDetail(movieId)
            movieService.fetchMovieCredits(movieId)
            movieService.fetchMovieImages(movieId)
        }
        expectThat(stateValues.last()).isEqualTo(
            MovieDetailViewModel.MovieDetailUiState(
                movieDetailMapper.map(movieDetailResponse),
                creditsMapper.map(creditsResponse),
                imageMapper.map(imagesResponse),
                loading = false
            )
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `fetchMovieDetail error`() = runTest {
        coEvery { movieService.fetchMovieDetail(movieId) } throws IOException()
        movieDetailViewModel = createViewModel()

        val stateValues = movieDetailViewModel.uiState.test()

        coVerify { movieService.fetchMovieDetail(movieId) }
        coVerify(inverse = true) {
            movieService.fetchMovieCredits(movieId)
            movieService.fetchMovieImages(movieId)
        }
        expectThat(stateValues.last().error).isA<IOException>()
    }

    private fun createViewModel() =
        MovieDetailViewModel(savedStateHandle, movieService, movieDetailMapper, creditsMapper, imageMapper)
}
