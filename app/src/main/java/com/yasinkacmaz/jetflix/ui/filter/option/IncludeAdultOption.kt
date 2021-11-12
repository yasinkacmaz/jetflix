package com.yasinkacmaz.jetflix.ui.filter.option

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.insets.navigationBarsPadding
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.filter.FilterSectionTitle
import com.yasinkacmaz.jetflix.ui.filter.FilterState

data class IncludeAdultOption(override val defaultValue: Boolean) : FilterOption<Boolean> {
    override var currentValue: Boolean = defaultValue

    override fun modifyFilterState(filterState: FilterState) =
        filterState.copy(includeAdult = currentValue)

    @Composable
    override fun Render(onChanged: () -> Unit) {
        val isChecked = remember(defaultValue) { mutableStateOf(currentValue) }
        val onClick = {
            currentValue = currentValue.not()
            isChecked.value = currentValue
            onChanged()
        }
        Row(
            Modifier.clickable(onClick = onClick).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterSectionTitle(painter = painterResource(id = R.drawable.ic_plus_18), title = R.string.include_adult)
            Switch(checked = isChecked.value, onCheckedChange = { onClick() })
        }
    }
}
