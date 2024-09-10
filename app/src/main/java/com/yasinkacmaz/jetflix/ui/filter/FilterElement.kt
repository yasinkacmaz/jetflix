package com.yasinkacmaz.jetflix.ui.filter

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.jetflix.ui.widget.VerticalStaggeredGrid

@Composable
fun FilterSectionTitle(painter: Painter, @StringRes title: Int) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painter, contentDescription = null, modifier = Modifier.size(26.dp))
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
fun FilterRadioItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton),
    ) {
        val radioIcon = if (selected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked
        val color = if (selected) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.6f,
            )
        }
        Icon(imageVector = radioIcon, contentDescription = null, tint = color)
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
fun <T : Any> FilterGrid(items: List<T>, itemContent: @Composable (Int, Modifier) -> Unit) = VerticalStaggeredGrid(
    itemCount = items.lastIndex,
    columnCount = 2,
    rowSpacing = 4.dp,
    columnSpacing = 6.dp,
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
    itemContent = itemContent,
)

@Composable
fun FilterSectionDivider() = HorizontalDivider(Modifier.padding(vertical = 4.dp))
