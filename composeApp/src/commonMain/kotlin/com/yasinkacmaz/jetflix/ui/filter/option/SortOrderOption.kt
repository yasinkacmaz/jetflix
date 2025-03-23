package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
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
import jetflix.composeapp.generated.resources.sort_order
import jetflix.composeapp.generated.resources.sort_order_ascending
import jetflix.composeapp.generated.resources.sort_order_descending
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class SortOrderOption(override val defaultValue: SortOrder) : FilterOption<SortOrder> {
    override var currentValue: SortOrder = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(sortOrder = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val sortOrderState = remember(defaultValue) { mutableStateOf(currentValue) }
        FilterSectionTitle(
            painter = rememberVectorPainter(image = Icons.Default.SwapVert),
            title = Res.string.sort_order,
        )
        val sortOrderValues = SortOrder.entries
        FilterGrid(sortOrderValues) { sortOrder ->
            val selected = sortOrderState.value == sortOrder
            FilterRadioItem(title = stringResource(sortOrder.title), selected = selected) {
                currentValue = sortOrder
                sortOrderState.value = currentValue
                onChanged()
            }
        }
        FilterSectionDivider()
    }
}

@Serializable
enum class SortOrder(val title: StringResource, val order: String) {
    DESCENDING(Res.string.sort_order_descending, "desc"),
    ASCENDING(Res.string.sort_order_ascending, "asc"),
}
