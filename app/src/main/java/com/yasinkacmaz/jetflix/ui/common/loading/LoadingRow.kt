package com.yasinkacmaz.jetflix.ui.common.loading

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun LoadingRow(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(modifier = Modifier.preferredSize(40.dp).padding(end = 8.dp))
        Text(title)
    }
}

@Preview(showDecoration = true)
@Composable
private fun LoadingRowPreview() {
    LoadingRow(title = "Please wait...")
}
