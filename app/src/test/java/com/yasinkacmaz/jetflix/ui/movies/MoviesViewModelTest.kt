package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.mockkRelaxed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MoviesViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService: MovieService = mockkRelaxed()
    private val filterDataStore: FilterDataStore = mockkRelaxed()

    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()

    private val loadParams = mockk<PagingSource.LoadParams<Int>> { every { key } returns 1 }
    private val moviesResponse = MoviesResponse(1, listOf(MovieResponse(1, "", "", "", "", "", "", 1.1, 1)), 1, 1)

    private val moviesViewModel = MoviesViewModel(movieService, movieMapper, movieRequestOptionsMapper, filterDataStore)

    @Before
    fun setUp() {
        every { filterDataStore.filterState } returns flowOf()
        moviesViewModel.initPagingSource()
    }

    @Test
    fun `fetchMovies success`() = coroutineTestRule.runBlockingTest {
        coEvery { movieService.fetchMovies(any(), any(), any()) } returns moviesResponse

        moviesViewModel.pagingSource.load(loadParams)

        coVerify { movieService.fetchMovies(any(), eq(1), any()) }
    }

    @Test
    fun `fetchMovies error`() = coroutineTestRule.runBlockingTest {
        val exception = IOException()
        coEvery { movieService.fetchMovies(any(), any(), any()) } throws exception

        moviesViewModel.pagingSource.load(loadParams)

        coVerify { movieService.fetchMovies(any(), eq(1), any()) }
    }
}
