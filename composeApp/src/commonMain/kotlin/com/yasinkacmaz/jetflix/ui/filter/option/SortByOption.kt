package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.filter.FilterGrid
import com.yasinkacmaz.jetflix.ui.filter.FilterRadioItem
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionDivider
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import kotlinx.serialization.Serializable

data class SortByOption(override val defaultValue: SortBy) : FilterOption<SortBy> {
    override var currentValue: SortBy = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortBy = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortByState = remember(defaultValue) { mutableStateOf(currentValue) }
        FilterSectionTitle(
            painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.Sort),
            title = R.string.sort_by,
        )
        val sortByValues = SortBy.entries
        FilterGrid(sortByValues) { sortBy ->
            val selected = sortByState.value == sortBy
            FilterRadioItem(title = stringResource(id = sortBy.titleResId), selected = selected) {
                currentValue = sortBy
                sortByState.value = sortBy
                onChanged()
            }
        }
        FilterSectionDivider()
    }
}

@Serializable
enum class SortBy(@StringRes val titleResId: Int, val by: String) {
    POPULARITY(R.string.sort_by_popularity, "popularity"),
    VOTE_COUNT(R.string.sort_by_vote_count, "vote_count"),
    VOTE_AVERAGE(R.string.sort_by_vote_average, "vote_average"),
    RELEASE_DATE(R.string.sort_by_release_date, "release_date"),
    ORIGINAL_TITLE(R.string.sort_by_original_title, "original_title"),
    REVENUE(R.string.sort_by_revenue, "revenue"),
}
