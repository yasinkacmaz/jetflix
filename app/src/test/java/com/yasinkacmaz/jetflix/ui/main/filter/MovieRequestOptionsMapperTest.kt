package com.yasinkacmaz.jetflix.ui.main.filter

import com.yasinkacmaz.jetflix.ui.main.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortOrder
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MovieRequestOptionsMapperTest {

    private val mapper = MovieRequestOptionsMapper()

    @Test
    fun map() {
        val filterState = FilterState(sortBy = SortBy.POPULARITY, sortOrder = SortOrder.ASCENDING, includeAdult = true)

        val options = mapper.map(filterState)

        val expectedOptions = filterState.toOptions()
        expectThat(options).isEqualTo(expectedOptions)
    }

    @Test
    fun `map should create options with default filter state when input is null`() {
        val options = mapper.map(null)

        val expectedOptions = FilterState().toOptions()
        expectThat(options).isEqualTo(expectedOptions)
    }

    private fun FilterState.toOptions() = mapOf(
        "sort_by" to "${sortBy.by}.${sortOrder.order}",
        "include_adult" to includeAdult.toString()
    )
}
