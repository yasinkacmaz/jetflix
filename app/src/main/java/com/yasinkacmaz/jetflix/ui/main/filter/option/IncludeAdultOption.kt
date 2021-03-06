package com.yasinkacmaz.jetflix.ui.main.filter.option

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.main.filter.FilterState

data class IncludeAdultOption(override val defaultValue: Boolean) : FilterOption<Boolean> {
    override var currentValue: Boolean = defaultValue

    override fun modifyFilterState(filterState: FilterState) =
        filterState.copy(includeAdult = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val isChecked = mutableStateOf(currentValue)
        val onClick = {
            currentValue = currentValue.not()
            isChecked.value = currentValue
            onChanged()
        }
        Row(
            Modifier.clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterSectionTitle(painter = painterResource(id = R.drawable.ic_plus_18), title = R.string.include_adult)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isChecked.value, onCheckedChange = { onClick() })
        }
        Divider(Modifier.padding(vertical = 8.dp))
    }
}
