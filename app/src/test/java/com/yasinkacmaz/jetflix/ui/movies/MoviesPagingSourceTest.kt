package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class MoviesPagingSourceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService = FakeMovieClient()

    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()
    private val filterState = FilterState()
    private val loadParams = PagingSource.LoadParams.Refresh(1, 1, true)

    lateinit var moviesPagingSource: MoviesPagingSource

    @Test
    fun `should call movies endpoint when query is empty`() = runTest {
        initPagingSource()

        val loadResult = moviesPagingSource.load(loadParams)

        expectThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = movieService.moviesResponse.movies.map(movieMapper::map),
                prevKey = null,
                nextKey = null
            )
        )
    }

    @Test
    fun `should call search endpoint when query is not empty`() = runTest {
        val query = "query"
        initPagingSource(query)

        val loadResult = moviesPagingSource.load(loadParams)

        expectThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = movieService.searchResponse.movies.map(movieMapper::map),
                prevKey = null,
                nextKey = null
            )
        )
    }

    private fun initPagingSource(query: String = "") {
        moviesPagingSource =
            MoviesPagingSource(movieService, movieMapper, movieRequestOptionsMapper, filterState, query)
    }
}
