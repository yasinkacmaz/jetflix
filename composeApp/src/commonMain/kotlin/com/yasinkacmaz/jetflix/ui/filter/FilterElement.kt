package com.yasinkacmaz.jetflix.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.spacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterSectionTitle(painter: Painter, title: StringResource) {
    Row(
        Modifier.padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painter, contentDescription = null, modifier = Modifier.size(28.dp))
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = MaterialTheme.spacing.s),
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
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        }
        Icon(imageVector = radioIcon, contentDescription = null, tint = color)
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = MaterialTheme.spacing.s),
        )
    }
}

@Composable
fun <T : Any> FilterGrid(items: List<T>, itemContent: @Composable (T) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s, Alignment.CenterVertically),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
    ) {
        items(items) {
            itemContent(it)
        }
    }
}

@Composable
fun FilterSectionDivider() = HorizontalDivider(Modifier.padding(vertical = MaterialTheme.spacing.xs))
