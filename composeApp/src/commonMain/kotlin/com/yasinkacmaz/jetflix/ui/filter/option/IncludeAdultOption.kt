package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoAdultContent
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.include_adult

data class IncludeAdultOption(override val defaultValue: Boolean) : FilterOption<Boolean> {
    override var currentValue: Boolean = defaultValue

    override fun modifyFilterState(filterState: FilterState) = filterState.copy(includeAdult = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val isChecked = remember(defaultValue) { mutableStateOf(currentValue) }
        val onClick = {
            currentValue = currentValue.not()
            isChecked.value = currentValue
            onChanged()
        }
        Row(
            Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilterSectionTitle(
                painter = rememberVectorPainter(Icons.Default.NoAdultContent),
                title = Res.string.include_adult,
            )
            Switch(checked = isChecked.value, onCheckedChange = { onClick() })
        }
    }
}
