package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.filter.FilterRadioItem
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.widget.VerticalStaggeredGrid
import kotlinx.serialization.Serializable

data class SortByOption(override val defaultValue: SortBy) : FilterOption<SortBy> {
    override var currentValue: SortBy = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortBy = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortByState = remember(defaultValue) { mutableStateOf(currentValue) }
        FilterSectionTitle(painter = rememberVectorPainter(image = Icons.Default.Sort), title = R.string.sort_by)
        val values = SortBy.values()
        VerticalStaggeredGrid(
            itemCount = values.lastIndex,
            columnCount = 2,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
        ) { index, _ ->
            val sortBy = values[index]
            val selected = sortByState.value == sortBy
            FilterRadioItem(title = stringResource(id = sortBy.titleResId), selected = selected) {
                currentValue = sortBy
                sortByState.value = sortBy
                onChanged()
            }
        }
        Divider(Modifier.padding(vertical = 8.dp))
    }
}

@Serializable
enum class SortBy(@StringRes val titleResId: Int, val by: String) {
    POPULARITY(R.string.sort_by_popularity, "popularity"),
    VOTE_COUNT(R.string.sort_by_vote_count, "vote_count"),
    VOTE_AVERAGE(R.string.sort_by_vote_average, "vote_average"),
    RELEASE_DATE(R.string.sort_by_release_date, "release_date"),
    ORIGINAL_TITLE(R.string.sort_by_original_title, "original_title"),
    REVENUE(R.string.sort_by_revenue, "revenue")
}
