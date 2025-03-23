package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.yasinkacmaz.jetflix.ui.filter.FilterGrid
import com.yasinkacmaz.jetflix.ui.filter.FilterRadioItem
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionDivider
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.sort_by
import jetflix.composeapp.generated.resources.sort_by_original_title
import jetflix.composeapp.generated.resources.sort_by_popularity
import jetflix.composeapp.generated.resources.sort_by_release_date
import jetflix.composeapp.generated.resources.sort_by_revenue
import jetflix.composeapp.generated.resources.sort_by_vote_average
import jetflix.composeapp.generated.resources.sort_by_vote_count
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class SortByOption(override val defaultValue: SortBy) : FilterOption<SortBy> {
    override var currentValue: SortBy = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortBy = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortByState = remember(defaultValue) { mutableStateOf(currentValue) }
        FilterSectionTitle(
            painter = rememberVectorPainter(image = Icons.AutoMirrored.Default.Sort),
            title = Res.string.sort_by,
        )
        val sortByValues = SortBy.entries
        FilterGrid(sortByValues) { sortBy ->
            val selected = sortByState.value == sortBy
            FilterRadioItem(title = stringResource(sortBy.title), selected = selected) {
                currentValue = sortBy
                sortByState.value = sortBy
                onChanged()
            }
        }
        FilterSectionDivider()
    }
}

@Serializable
enum class SortBy(val title: StringResource, val by: String) {
    POPULARITY(Res.string.sort_by_popularity, "popularity"),
    VOTE_COUNT(Res.string.sort_by_vote_count, "vote_count"),
    VOTE_AVERAGE(Res.string.sort_by_vote_average, "vote_average"),
    RELEASE_DATE(Res.string.sort_by_release_date, "release_date"),
    ORIGINAL_TITLE(Res.string.sort_by_original_title, "original_title"),
    REVENUE(Res.string.sort_by_revenue, "revenue"),
}
