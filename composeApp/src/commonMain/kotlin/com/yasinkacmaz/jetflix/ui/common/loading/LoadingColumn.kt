package com.yasinkacmaz.jetflix.ui.common.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.spacing

@Composable
fun LoadingColumn(title: String, modifier: Modifier = Modifier) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(title)
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = MaterialTheme.spacing.l),
            )
        }
    }
}
