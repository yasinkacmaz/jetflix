package com.yasinkacmaz.jetflix.ui.main.filter

import com.yasinkacmaz.jetflix.ui.main.filter.option.FilterOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.IncludeAdultOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortByOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortOrder
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortOrderOption
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterState(
    @SerialName("sort_order") val sortOrder: SortOrder = SortOrder.DESCENDING,
    @SerialName("sort_by") val sortBy: SortBy = SortBy.POPULARITY,
    @SerialName("includeAdult") val includeAdult: Boolean = false
)

fun FilterState.toFilterOptions(): List<FilterOption<*>> = listOf(
    SortOrderOption(sortOrder),
    SortByOption(sortBy),
    IncludeAdultOption(includeAdult)
)
