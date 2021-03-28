package com.yasinkacmaz.jetflix.ui.main.filter

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.ui.main.filter.FilterDataStore.Companion.KEY_FILTER_STATE
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortBy
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.json
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.encodeToString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FilterDataStoreTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var preferencesDataStore: DataStore<Preferences>

    @RelaxedMockK
    private lateinit var preferences: Preferences

    private lateinit var filterDataStore: FilterDataStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { preferencesDataStore.data } returns flowOf(preferences)
        every { preferences[KEY_FILTER_STATE] } returns null
        filterDataStore = FilterDataStore(json, preferencesDataStore)
    }

    @Test
    fun `Should set filterState as default when preference is not exists`() = coroutineTestRule.runBlockingTest {
        every { preferences[KEY_FILTER_STATE] } returns null

        expectThat(filterDataStore.filterState.first()).isEqualTo(FilterState(includeAdult = true))
    }

    @Test
    fun `Should set filterState when preference is exists`() = coroutineTestRule.runBlockingTest {
        val filterState = FilterState(sortBy = SortBy.VOTE_AVERAGE)
        every { preferences[KEY_FILTER_STATE] } returns json.encodeToString(filterState)

        expectThat(filterDataStore.filterState.first()).isEqualTo(filterState)
    }
}
