package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun CircleIconButton(modifier: Modifier = Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(
        modifier = modifier
            .shadow(2.dp, shape = CircleShape)
            .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
        onClick = onClick,
    ) {
        content()
    }
}
