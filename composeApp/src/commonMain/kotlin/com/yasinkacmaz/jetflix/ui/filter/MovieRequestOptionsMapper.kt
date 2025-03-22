package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.util.Mapper

class MovieRequestOptionsMapper : Mapper<FilterState?, Map<String, String>> {
    override fun map(input: FilterState?): Map<String, String> = buildMap {
        val filterState = input ?: FilterState()
        val sortBy = "${filterState.sortBy.by}.${filterState.sortOrder.order}"
        put(SORT_BY, sortBy)
        val includeAdult = filterState.includeAdult.toString()
        put(INCLUDE_ADULT, includeAdult)
        if (filterState.selectedGenreIds.isNotEmpty()) {
            val selectedGenreIds = filterState.selectedGenreIds.joinToString("|")
            put(WITH_GENRES, selectedGenreIds)
        }
    }

    companion object {
        private const val SORT_BY = "sort_by"
        private const val INCLUDE_ADULT = "include_adult"
        private const val WITH_GENRES = "with_genres"
    }
}
