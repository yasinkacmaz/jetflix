package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
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
    private val loadParams = mockk<PagingSource.LoadParams<Int>> {
        every { key } returns 1
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        moviesViewModel = MoviesViewModel(movieService, movieMapper)
        moviesViewModel.createPagingSource(genreId)
    }

    @Test
    fun `fetchMovies success`() = coroutineTestRule.runBlockingTest {
        val movieResponse = MovieResponse(1, "", "", "", "", "", "", 1.1, 1)
        val response = MoviesResponse(1, listOf(movieResponse), 1, 1)
        coEvery { movieService.fetchMovies(eq(genreId), any(), any()) } returns response

        moviesViewModel.pagingSource.load(loadParams)

        coVerify { movieService.fetchMovies(eq(genreId), eq(1), any()) }
    }

    @Test
    fun `fetchMovies error`() = coroutineTestRule.runBlockingTest {
        val exception = IOException()
        coEvery { movieService.fetchMovies(eq(genreId), any(), any()) } throws exception

        moviesViewModel.pagingSource.load(loadParams)

        coVerify { movieService.fetchMovies(eq(genreId), eq(1), any()) }
    }
}
