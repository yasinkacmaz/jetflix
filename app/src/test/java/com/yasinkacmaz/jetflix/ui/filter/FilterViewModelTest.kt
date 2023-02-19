package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeMovieService
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import com.yasinkacmaz.jetflix.util.testDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class FilterViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val fakeStringDataStore = FakeStringDataStore()
    private val filterDataStore = FilterDataStore(json, fakeStringDataStore)
    private val movieService = FakeMovieService()
    private val genreUiModelMapper = GenreUiModelMapper()
    private val genreUiModel = genreUiModelMapper.map(movieService.genre)

    @Test
    fun `Should fetch genres`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeStringDataStore.set(filterState)

        val filterViewModel = createViewModel()

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState.copy(genres = listOf(genreUiModel)))
    }

    @Test
    fun `Should set genres as empty when fetch genres error`() = runTest {
        movieService.fetchGenresShouldFail = true
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeStringDataStore.set(filterState)

        val filterViewModel = createViewModel()

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState.copy(genres = emptyList()))
        movieService.fetchGenresShouldFail = false
    }

    @Test
    fun `onResetClicked should call data store resetFilterState`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeStringDataStore.set(filterState)

        val filterViewModel = createViewModel()
        val filterStates = filterViewModel.filterState.test()
        filterViewModel.onResetClicked()

        expectThat(filterStates[0]).isEqualTo(filterState.copy(genres = listOf(genreUiModel)))
        expectThat(filterStates[1]).isEqualTo(FilterState().copy(genres = listOf(genreUiModel)))
    }

    @Test
    fun `onFilterStateChanged should call data store onFilterStateChanged`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeStringDataStore.set(filterState)

        val newFilterState = FilterState(sortBy = SortBy.VOTE_AVERAGE)

        val filterViewModel = createViewModel()
        val filterStates = filterViewModel.filterState.test()

        filterViewModel.onFilterStateChanged(newFilterState)

        expectThat(filterStates[0]).isEqualTo(filterState.copy(genres = listOf(genreUiModel)))
        expectThat(filterStates[1]).isEqualTo(newFilterState.copy(genres = listOf(genreUiModel)))
    }

    @Test
    fun `filter state should change when filter data store changed`() = runTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        fakeStringDataStore.set(filterState)

        val filterViewModel = createViewModel()
        val filterStates = filterViewModel.filterState.test()

        val changedFilterState = FilterState(sortBy = SortBy.RELEASE_DATE)
        fakeStringDataStore.set(changedFilterState)

        expectThat(filterStates.last()).isEqualTo(changedFilterState.copy(genres = listOf(genreUiModel)))
    }

    private fun createViewModel() = FilterViewModel(filterDataStore, movieService, genreUiModelMapper, testDispatchers)
}
