package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
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

data class SortOrderOption(override val defaultValue: SortOrder) : FilterOption<SortOrder> {
    override var currentValue: SortOrder = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortOrder = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortOrderState = remember(defaultValue) { mutableStateOf(currentValue) }
        FilterSectionTitle(
            painter = rememberVectorPainter(image = Icons.Default.SwapVert),
            title = R.string.sort_order,
        )
        val sortOrderValues = SortOrder.values().toList()
        FilterGrid(items = sortOrderValues) { index, _ ->
            val sortOrder = sortOrderValues[index]
            val selected = sortOrderState.value == sortOrder
            FilterRadioItem(title = stringResource(id = sortOrder.titleResId), selected = selected) {
                currentValue = sortOrder
                sortOrderState.value = currentValue
                onChanged()
            }
        }
        FilterSectionDivider()
    }
}

@Serializable
enum class SortOrder(@StringRes val titleResId: Int, val order: String) {
    DESCENDING(R.string.sort_order_descending, "desc"),
    ASCENDING(R.string.sort_order_ascending, "asc"),
}
