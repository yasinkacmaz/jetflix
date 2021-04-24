package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.mockkRelaxed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class MoviesPagingSourceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService: MovieService = mockkRelaxed()
    private val loadParams = mockk<PagingSource.LoadParams<Int>> { every { key } returns 1 }
    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()
    private val filterState = FilterState()
    private val genreId = 1337

    private val moviesPagingSource =
        MoviesPagingSource(movieService, movieMapper, movieRequestOptionsMapper, filterState, genreId)

    @Test
    fun load() = coroutineTestRule.runBlockingTest {
        val moviesResponse = MoviesResponse(1, listOf(MovieResponse(1, "", "", "", "", "", "", 1.1, 1)), 1, 1)
        coEvery { movieService.fetchMovies(any(), any(), any()) } returns moviesResponse

        moviesPagingSource.load(loadParams)

        val expectedOptions = movieRequestOptionsMapper.map(filterState)
        coVerify { movieService.fetchMovies(genreId, 1, expectedOptions) }
    }
}
