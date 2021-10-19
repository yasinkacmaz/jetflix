package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MoviesViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

    @MockK
    private lateinit var filterDataStore: FilterDataStore

    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()

    private lateinit var moviesViewModel: MoviesViewModel
    private val loadParams = mockk<PagingSource.LoadParams<Int>> {
        every { key } returns 1
    }

    private val moviesResponse = MoviesResponse(1, listOf(MovieResponse(1, "", "", "", "", "", "", 1.1, 1)), 1, 1)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { filterDataStore.filterState } returns flowOf()
        moviesViewModel = MoviesViewModel(movieService, movieMapper, movieRequestOptionsMapper, filterDataStore)
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
