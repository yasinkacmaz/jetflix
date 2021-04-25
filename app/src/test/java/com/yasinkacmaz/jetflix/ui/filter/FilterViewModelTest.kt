package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.GenresResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.IOException

class FilterViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var filterDataStore: FilterDataStore

    @RelaxedMockK
    private lateinit var movieService: MovieService

    @RelaxedMockK
    private lateinit var genreUiModelMapper: GenreUiModelMapper

    private lateinit var filterViewModel: FilterViewModel
    private val genre = Genre(1, "Name")
    private val genreUiModel = GenreUiModel(genre = genre)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { filterDataStore.filterState } returns flowOf()
        every { genreUiModelMapper.map(genre) } returns genreUiModel
        coEvery { movieService.fetchGenres() } returns GenresResponse(listOf(genre))
        filterViewModel = FilterViewModel(filterDataStore, movieService, genreUiModelMapper)
    }

    @Test
    fun `Should fetch genres`() = coroutineTestRule.runBlockingTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        coEvery { filterDataStore.filterState } returns flowOf(filterState)

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState.copy(genres = listOf(genreUiModel)))
        coVerify { filterDataStore.filterState }
        coVerify { movieService.fetchGenres() }
    }

    @Test
    fun `Should fetch genres only once`() = coroutineTestRule.runBlockingTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        every { filterDataStore.filterState } returns flowOf(filterState, filterState, filterState)

        // Init lazy filter state by accessing it
        filterViewModel.filterState

        coVerify(exactly = 1) { movieService.fetchGenres() }
    }

    @Test
    fun `Should set genres as empty when fetch genres error`() = coroutineTestRule.runBlockingTest {
        coEvery { movieService.fetchGenres() } throws IOException()
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        coEvery { filterDataStore.filterState } returns flowOf(filterState)

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState.copy(genres = emptyList()))
    }

    @Test
    fun `onResetClicked should call data store resetFilterState`() = coroutineTestRule.runBlockingTest {
        filterViewModel.onResetClicked()

        coVerify { filterDataStore.resetFilterState() }
    }

    @Test
    fun `onFilterStateChanged should call data store onFilterStateChanged`() = coroutineTestRule.runBlockingTest {
        val newFilterState = FilterState(sortBy = SortBy.REVENUE)

        filterViewModel.onFilterStateChanged(newFilterState)

        coVerify { filterDataStore.onFilterStateChanged(newFilterState) }
    }

    @Test
    fun `filter state should change when filter data store changed`() = coroutineTestRule.runBlockingTest {
        val filterState = FilterState(sortBy = SortBy.REVENUE)
        coEvery { filterDataStore.filterState } returns flowOf(filterState)

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState.copy(genres = listOf(genreUiModel)))
    }
}
