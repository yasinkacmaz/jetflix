package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MoviesPagingSourceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService = FakeMovieClient()

    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()
    private val filterState = FilterState()
    private val loadParams = PagingSource.LoadParams.Refresh(1, 1, true)

    private lateinit var moviesPagingSource: MoviesPagingSource

    @Test
    fun `should call movies endpoint when query is empty`() = runTest {
        initPagingSource()

        val loadResult = moviesPagingSource.load(loadParams)

        loadResult.shouldBeInstanceOf<PagingSource.LoadResult.Page<Int, Movie>>()
        loadResult.data shouldBe movieService.moviesResponse.movies.map(movieMapper::map)
    }

    @Test
    fun `should call search endpoint when query is not empty`() = runTest {
        val query = "query"
        initPagingSource(query)

        val loadResult = moviesPagingSource.load(loadParams)

        loadResult.shouldBeInstanceOf<PagingSource.LoadResult.Page<Int, Movie>>()
        loadResult.data shouldBe movieService.searchResponse.movies.map(movieMapper::map)
    }

    private fun initPagingSource(query: String = "") {
        moviesPagingSource =
            MoviesPagingSource(movieService, movieMapper, movieRequestOptionsMapper, filterState, query)
    }
}
