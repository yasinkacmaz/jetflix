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
import kotlinx.coroutines.test.runTest
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
    }

    @Test
    fun `should call search endpoint when query is not empty`() = runTest {
        val query = "query"
        coEvery { movieService.search(any(), any()) } returns moviesResponse
        moviesViewModel.onSearch(query)

        loadMovies()

        coVerify { movieService.search(eq(1), eq(query), eq(true)) }
        coVerify(exactly = 0) { movieService.fetchMovies(eq(1), any()) }
    }

    @Test
    fun `should call movies endpoint when query is empty`() = runTest {
        coEvery { movieService.fetchMovies(any(), any()) } returns moviesResponse

        loadMovies()

        coVerify { movieService.fetchMovies(eq(1), any()) }
        coVerify(exactly = 0) { movieService.search(eq(1), any()) }
    }

    @Test
    fun `fetchMovies error`() = runTest {
        val exception = IOException()
        coEvery { movieService.fetchMovies(any(), any()) } throws exception

        loadMovies()

        coVerify { movieService.fetchMovies(eq(1), any()) }
    }

    private suspend fun loadMovies() {
        moviesViewModel.initPagingSource()
        moviesViewModel.pagingSource.load(loadParams)
    }
}
