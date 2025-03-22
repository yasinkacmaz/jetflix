package com.yasinkacmaz.jetflix.ui.movies

import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val movieService = FakeMovieClient()
    private val filterDataStore = FilterDataStore(json, FakeStringDataStore())
    private val languageDataStore = LanguageDataStore(json, FakeStringDataStore())
    private val movieMapper = MovieMapper()
    private val movieRequestOptionsMapper = MovieRequestOptionsMapper()

    @Test
    fun `Should not send search query change event when query is empty`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQueryChanges.test()

        moviesViewModel.onSearch("")

        queryChanges.shouldBeEmpty()
    }

    @Test
    fun `Should not send search query change event when query is less than threshold`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQueryChanges.test()

        moviesViewModel.onSearch("qu")

        queryChanges.shouldBeEmpty()
    }

    @Test
    fun `Should send search query change event when query is valid`() = runTest {
        val moviesViewModel = createViewModel()
        val queryChanges = moviesViewModel.searchQueryChanges.test()

        val query = "query"
        moviesViewModel.onSearch(query)

        queryChanges.shouldNotBeEmpty()
        queryChanges.last() shouldBe query
    }

    @Test
    fun `Should set search query when searched`() = runTest {
        val moviesViewModel = createViewModel()

        val query = "query"
        moviesViewModel.onSearch(query)

        moviesViewModel.searchQuery.value shouldBe query
    }

    private fun createViewModel() =
        MoviesViewModel(movieService, movieMapper, movieRequestOptionsMapper, filterDataStore, languageDataStore)
}
