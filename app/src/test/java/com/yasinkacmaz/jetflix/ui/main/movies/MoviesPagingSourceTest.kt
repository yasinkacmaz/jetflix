package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.filter.FilterState
import com.yasinkacmaz.jetflix.ui.main.filter.MovieRequestOptionsMapper
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

class MoviesPagingSourceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

    lateinit var moviesPagingSource: MoviesPagingSource
    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()
    private val filterState = FilterState()
    private val genreId = 1337
    private val loadParams = mockk<PagingSource.LoadParams<Int>> {
        every { key } returns 1
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        moviesPagingSource =
            MoviesPagingSource(movieService, movieMapper, movieRequestOptionsMapper, filterState, genreId)
    }

    @Test
    fun load() = coroutineTestRule.runBlockingTest {
        val moviesResponse = MoviesResponse(1, listOf(MovieResponse(1, "", "", "", "", "", "", 1.1, 1)), 1, 1)
        coEvery { movieService.fetchMovies(any(), any(), any()) } returns moviesResponse

        moviesPagingSource.load(loadParams)

        val expectedOptions = movieRequestOptionsMapper.map(filterState)
        coVerify { movieService.fetchMovies(genreId, 1, expectedOptions) }
    }
}
