package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.ui.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.filter.option.SortOrder
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MovieRequestOptionsMapperTest {

    private val mapper = MovieRequestOptionsMapper()

    @Test
    fun map() {
        val filterState = FilterState(
            sortBy = SortBy.POPULARITY,
            sortOrder = SortOrder.ASCENDING,
            includeAdult = true,
            selectedGenreIds = listOf(1, 2),
        )

        val options = mapper.map(filterState)

        val expectedOptions = mapOf(
            "sort_by" to "${filterState.sortBy.by}.${filterState.sortOrder.order}",
            "include_adult" to filterState.includeAdult.toString(),
            "with_genres" to filterState.selectedGenreIds.joinToString("|"),
        )
        options shouldBe expectedOptions
    }

    @Test
    fun `map should create options with default filter state when input is null`() {
        val options = mapper.map(null)

        val defaultFilterState = FilterState()
        val expectedOptions = mapOf(
            "sort_by" to "${defaultFilterState.sortBy.by}.${defaultFilterState.sortOrder.order}",
            "include_adult" to defaultFilterState.includeAdult.toString(),
        )
        options shouldBe expectedOptions
    }
}
