package com.yasinkacmaz.jetflix.ui.main.filter

import com.yasinkacmaz.jetflix.ui.main.filter.option.FilterOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.GenresFilterOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.GenresOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.IncludeAdultOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortBy
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortByOption
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortOrder
import com.yasinkacmaz.jetflix.ui.main.filter.option.SortOrderOption
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class FilterState(
    @SerialName("sort_order") val sortOrder: SortOrder = SortOrder.DESCENDING,
    @SerialName("sort_by") val sortBy: SortBy = SortBy.POPULARITY,
    @SerialName("includeAdult") val includeAdult: Boolean = false,
    @SerialName("selected_genre_ids") val selectedGenreIds: List<Int> = emptyList(),
    @Transient val genres: List<GenreUiModel> = emptyList()
)

@OptIn(ExperimentalStdlibApi::class)
fun FilterState.toFilterOptions(): List<FilterOption<*>> = buildList {
    add(SortOrderOption(sortOrder))
    add(SortByOption(sortBy))
    if (genres.isNotEmpty()) {
        add(GenresOption(GenresFilterOption(genres, selectedGenreIds.toMutableList())))
    }
    add(IncludeAdultOption(includeAdult))
}
