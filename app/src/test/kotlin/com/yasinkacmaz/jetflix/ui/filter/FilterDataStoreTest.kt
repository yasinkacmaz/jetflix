package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.json
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FilterDataStoreTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val fakeStringDataStore = FakeStringDataStore()

    @Test
    fun `Should set filterState as default when preference is not exists`() = runTest {
        fakeStringDataStore.set("", "")

        val filterDataStore = createFilterDataStore()

        filterDataStore.filterState.first() shouldBe FilterState()
    }

    @Test
    fun `Should set filterState when preference is exists`() = runTest {
        val filterState = FilterState(sortBy = SortBy.VOTE_AVERAGE)
        fakeStringDataStore.set(filterState)

        val filterDataStore = createFilterDataStore()

        filterDataStore.filterState.first() shouldBe filterState
    }

    private fun createFilterDataStore() = FilterDataStore(json, fakeStringDataStore)
}
