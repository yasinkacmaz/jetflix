package com.yasinkacmaz.jetflix.ui.main.filter.option

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.main.filter.FilterRadioItem
import com.yasinkacmaz.jetflix.ui.main.filter.FilterState
import com.yasinkacmaz.jetflix.ui.widget.VerticalStaggeredGrid
import kotlinx.serialization.Serializable

data class SortOrderOption(override val defaultValue: SortOrder) : FilterOption<SortOrder> {
    override var currentValue: SortOrder = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortOrder = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortOrderState = mutableStateOf(currentValue)
        FilterSectionTitle(
            painter = rememberVectorPainter(image = Icons.Default.SwapVert),
            title = R.string.sort_order
        )
        val values = SortOrder.values()
        VerticalStaggeredGrid(
            itemCount = values.lastIndex,
            columnCount = 2,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
        ) { index, _ ->
            val sortOrder = values[index]
            val selected = sortOrderState.value == sortOrder
            FilterRadioItem(title = stringResource(id = sortOrder.titleResId), selected = selected) {
                currentValue = sortOrder
                sortOrderState.value = currentValue
                onChanged()
            }
        }
        Divider(Modifier.padding(vertical = 8.dp))
    }
}

@Serializable
enum class SortOrder(@StringRes val titleResId: Int, val order: String) {
    DESCENDING(R.string.sort_order_descending, "desc"),
    ASCENDING(R.string.sort_order_ascending, "asc")
}
