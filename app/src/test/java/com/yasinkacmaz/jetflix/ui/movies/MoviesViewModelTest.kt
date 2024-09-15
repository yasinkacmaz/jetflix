package com.yasinkacmaz.jetflix.ui.movies

import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService = FakeMovieClient()
    private val filterDataStore = FilterDataStore(json, FakeStringDataStore())
    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()

    @Test
    fun `Should not set search query when query is empty`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQuery.test()

        moviesViewModel.onSearch("")

        queryChanges.last() shouldBe ""
    }

    @Test
    fun `Should not set search query when query is less than threshold`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQuery.test()

        moviesViewModel.onSearch("qu")

        queryChanges.last() shouldBe ""
    }

    @Test
    fun `Should set search query when query is valid`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQuery.test()

        val query = "query"
        moviesViewModel.onSearch(query)

        queryChanges.last() shouldBe query
    }

    private fun createViewModel() =
        MoviesViewModel(movieService, movieMapper, movieRequestOptionsMapper, filterDataStore)
}
