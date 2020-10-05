package com.yasinkacmaz.jetflix.ui.main.common

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun ErrorRow(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Face, modifier = Modifier.preferredSize(40.dp).padding(end = 8.dp))
        Text(title)
    }
}

@Preview(showDecoration = true)
@Composable
fun ErrorRowPreview() {
    ErrorRow(title = "Oopsie!")
}
