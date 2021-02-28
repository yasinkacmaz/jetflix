package com.yasinkacmaz.jetflix.ui.main.filter.option

import androidx.compose.runtime.Composable
import com.yasinkacmaz.jetflix.ui.main.filter.FilterState

interface FilterOption<Type : Any> {
    var value: Type

    fun modifyFilterState(filterState: FilterState, currentValue: Type = value): FilterState

    @Composable fun Render(onChanged: () -> Unit)
}
