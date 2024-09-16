package com.yasinkacmaz.jetflix.ui.moviedetail

import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MovieDetailViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService = FakeMovieClient()

    private val movieId = 1337

    private val movieDetailMapper = MovieDetailMapper()
    private val creditsMapper = CreditsMapper()
    private val imageMapper = ImageMapper()

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @Test
    fun `fetchMovieDetail success`() = runTest {
        movieDetailViewModel = createViewModel()

        val stateValues = movieDetailViewModel.uiState.test()

        val expectedState = MovieDetailViewModel.MovieDetailUiState(
            movieDetailMapper.map(movieService.movieDetailResponse),
            creditsMapper.map(movieService.creditsResponse),
            imageMapper.map(movieService.imagesResponse),
            loading = false,
        )
        stateValues.last() shouldBe expectedState
    }

    @Test
    fun `fetchMovieDetail error`() = runTest {
        movieService.movieDetailException = IOException()

        movieDetailViewModel = createViewModel()
        val stateValues = movieDetailViewModel.uiState.test()

        stateValues.last().error.shouldBeInstanceOf<IOException>()
    }

    private fun createViewModel() =
        MovieDetailViewModel(movieId, movieService, movieDetailMapper, creditsMapper, imageMapper)
}
