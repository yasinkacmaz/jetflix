package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import com.yasinkacmaz.jetflix.util.testDispatchers
import io.kotest.matchers.shouldBe
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FilterViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val fakeFilterDataStore = FakeStringDataStore()
    private val filterDataStore = FilterDataStore(json, fakeFilterDataStore)
    private val movieService = FakeMovieClient()
    private val genreUiModelMapper = GenreUiModelMapper()
    private val genreUiModel = genreUiModelMapper.map(movieService.genre)

    @Test
    fun `Should fetch genres`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeFilterDataStore.set(filterState)

        val filterViewModel = createViewModel()

        filterViewModel.filterState.first() shouldBe filterState.copy(genres = listOf(genreUiModel))
    }

    @Test
    fun `Should set genres as empty when fetch genres error`() = runTest {
        movieService.fetchGenresException = IOException()
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeFilterDataStore.set(filterState)

        val filterViewModel = createViewModel()

        filterViewModel.filterState.first() shouldBe filterState.copy(genres = emptyList())
    }

    @Test
    fun `onFilterStateChanged should call data store onFilterStateChanged`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeFilterDataStore.set(filterState)

        val newFilterState = FilterState(sortBy = SortBy.VOTE_AVERAGE)

        val filterViewModel = createViewModel()
        val filterStates = filterViewModel.filterState.test()

        filterViewModel.onFilterStateChanged(newFilterState)

        filterStates[0] shouldBe filterState.copy(genres = listOf(genreUiModel))
        filterStates[1] shouldBe newFilterState.copy(genres = listOf(genreUiModel))
    }

    @Test
    fun `Should update filter state when filter data store changed`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeFilterDataStore.set(filterState)

        val filterViewModel = createViewModel()
        val filterStates = filterViewModel.filterState.test()

        val changedFilterState = FilterState(sortBy = SortBy.RELEASE_DATE)
        fakeFilterDataStore.set(changedFilterState)

        filterStates.last() shouldBe changedFilterState.copy(genres = listOf(genreUiModel))
    }

    private fun createViewModel() = FilterViewModel(filterDataStore, movieService, genreUiModelMapper, testDispatchers)
}
