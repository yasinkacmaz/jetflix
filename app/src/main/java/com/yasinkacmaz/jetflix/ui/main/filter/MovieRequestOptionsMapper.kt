package com.yasinkacmaz.jetflix.ui.main.filter

import com.yasinkacmaz.jetflix.util.Mapper
import javax.inject.Inject

class MovieRequestOptionsMapper @Inject constructor() : Mapper<FilterState?, Map<String, String>> {
    override fun map(input: FilterState?): Map<String, String> {
        val filterState = input ?: FilterState()
        val sortBy = "${filterState.sortBy.by}.${filterState.sortOrder.order}"
        val includeAdult = filterState.includeAdult.toString()
        return mapOf(SORT_BY to sortBy, INCLUDE_ADULT to includeAdult)
    }

    companion object {
        private const val SORT_BY = "sort_by"
        private const val INCLUDE_ADULT = "include_adult"
    }
}
