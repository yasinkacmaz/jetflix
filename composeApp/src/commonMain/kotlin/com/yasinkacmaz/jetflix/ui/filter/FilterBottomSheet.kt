package com.yasinkacmaz.jetflix.ui.filter

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(filterState: FilterState?, onDismiss: () -> Unit, onFilterStateChanged: (FilterState) -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        scrimColor = BottomSheetDefaults.ScrimColor.copy(alpha = 0.75f),
        content = {
            if (filterState == null) {
                LoadingRow(title = stringResource(id = R.string.loading_filter_options))
            } else {
                val filterOptions = remember(filterState.genres.size) { filterState.toFilterOptions() }
                filterOptions.forEach { filterOption ->
                    filterOption.Render {
                        val newState = filterOption.modifyFilterState(filterState)
                        onFilterStateChanged(newState)
                    }
                }
            }
        },
    )
}
