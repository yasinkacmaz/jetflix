package com.yasinkacmaz.jetflix.ui.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview(showSystemUi = true)
@Composable
private fun LoadingRowPreview() {
    LoadingRow(title = "Please wait...")
}
