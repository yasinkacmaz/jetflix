package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.lifecycle.SavedStateHandle
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.ui.navigation.ARG_MOVIE_ID
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeMovieService
import com.yasinkacmaz.jetflix.util.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

    private val movieService = FakeMovieService()

    private val movieId = 1337

    private val savedStateHandle = SavedStateHandle(mapOf(ARG_MOVIE_ID to movieId.toString()))

    private val movieDetailMapper = MovieDetailMapper()
    private val creditsMapper = CreditsMapper()
    private val imageMapper = ImageMapper()

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @Test
    fun `fetchMovieDetail success`() = runTest {
        movieDetailViewModel = createViewModel()

        val stateValues = movieDetailViewModel.uiState.test()

        expectThat(stateValues.last()).isEqualTo(
            MovieDetailViewModel.MovieDetailUiState(
                movieDetailMapper.map(movieService.movieDetailResponse),
                creditsMapper.map(movieService.creditsResponse),
                imageMapper.map(movieService.imagesResponse),
                loading = false
            )
        )
    }

    @Test
    fun `fetchMovieDetail error`() = runTest {
        movieService.movieDetailException = IOException()

        movieDetailViewModel = createViewModel()
        val stateValues = movieDetailViewModel.uiState.test()

        expectThat(stateValues.last().error).isA<IOException>()
    }

    private fun createViewModel() =
        MovieDetailViewModel(savedStateHandle, movieService, movieDetailMapper, creditsMapper, imageMapper)
}
