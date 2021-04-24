package com.yasinkacmaz.jetflix.ui.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.yasinkacmaz.jetflix.R

@Suppress("unused")
@Composable
fun ColumnScope.FilterContent(
    filterState: FilterState,
    onFilterStateChanged: (FilterState) -> Unit,
    onResetClicked: () -> Unit,
    onHideClicked: () -> Unit
) {
    FilterHeader(onHideClicked, onResetClicked)
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 8.dp)
    ) {
        filterState.toFilterOptions().forEach { filterOption ->
            filterOption.Render {
                val newState = filterOption.modifyFilterState(filterState)
                onFilterStateChanged(newState)
            }
        }
    }
}

@Composable
private fun FilterHeader(onHideClicked: () -> Unit, onResetClicked: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth(),
        elevation = 8.dp,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onHideClicked) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_content_description)
                    )
                }
                Text(
                    text = stringResource(id = R.string.title_filter_bottom_sheet),
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Text(
                stringResource(id = R.string.reset),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.clickable { onResetClicked() }
            )
        }
    }
}
