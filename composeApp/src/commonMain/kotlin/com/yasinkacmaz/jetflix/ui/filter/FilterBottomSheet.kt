package com.yasinkacmaz.jetflix.ui.filter

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.yasinkacmaz.jetflix.ui.common.Loading
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.loading_filter_options
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(filterState: FilterState?, onDismiss: () -> Unit, onFilterStateChanged: (FilterState) -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        scrimColor = BottomSheetDefaults.ScrimColor.copy(alpha = 0.75f),
        content = {
            if (filterState == null) {
                Loading(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.loading_filter_options),
                )
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
