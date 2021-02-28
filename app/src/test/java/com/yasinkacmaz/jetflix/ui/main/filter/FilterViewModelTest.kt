package com.yasinkacmaz.jetflix.ui.main.filter

import com.yasinkacmaz.jetflix.ui.main.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FilterViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var filterDataStore: FilterDataStore

    private lateinit var filterViewModel: FilterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { filterDataStore.filterState } returns flowOf()
        filterViewModel = FilterViewModel(filterDataStore)
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

        expectThat(filterViewModel.filterState.first()).isEqualTo(filterState)
    }
}
