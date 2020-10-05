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
fun LoadingColumn(title: String, modifier: Modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)
        CircularProgressIndicator(modifier = Modifier.preferredSize(40.dp).padding(top = 16.dp))
    }
}

@Preview(showDecoration = true)
@Composable
private fun LoadingColumnPreview() {
    LoadingColumn(title = "Please wait...")
}
