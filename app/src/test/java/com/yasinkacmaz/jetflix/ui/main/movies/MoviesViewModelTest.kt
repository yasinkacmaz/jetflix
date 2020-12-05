package com.yasinkacmaz.jetflix.ui.main.movies

import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MoviesViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

    private val movieMapper = MovieMapper()

    private lateinit var moviesViewModel: MoviesViewModel

    private val genreId = 1337

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        moviesViewModel = spyk(MoviesViewModel(movieService, movieMapper))
    }

    @Test
    fun `fetchMovies success`() = coroutineTestRule.runBlockingTest {
        val movieResponse = MovieResponse(1, "", "", "", "", "", "", 1.1, 1)
        val response = MoviesResponse(1, listOf(movieResponse), 1, 1)
        coEvery { movieService.fetchMovies(eq(genreId), any(), any()) } returns response

        moviesViewModel.fetchMovies(genreId)

        coVerify { movieService.fetchMovies(eq(genreId), any(), any()) }
        verifyOrder {
            moviesViewModel.uiValue = MoviesViewModel.MovieUiState(loading = true)
            moviesViewModel.uiValue =
                MoviesViewModel.MovieUiState(loading = false, movies = mutableListOf(movieMapper.map(movieResponse)))
        }
    }

    @Test
    fun `fetchMovies error`() = coroutineTestRule.runBlockingTest {
        val exception = IOException()
        coEvery { movieService.fetchMovies(eq(genreId), any(), any()) } throws exception

        moviesViewModel.fetchMovies(genreId)

        coVerify { movieService.fetchMovies(eq(genreId), any(), any()) }
        verifyOrder {
            moviesViewModel.uiValue = MoviesViewModel.MovieUiState(loading = true)
            moviesViewModel.uiValue = MoviesViewModel.MovieUiState(loading = false, error = exception)
        }
    }
}
