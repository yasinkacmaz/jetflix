package com.yasinkacmaz.jetflix.ui.common.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.spacing
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.error_icon_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorColumn(message: String, modifier: Modifier = Modifier) {
    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(message)
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = stringResource(Res.string.error_icon_content_description),
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = MaterialTheme.spacing.l),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorColumnPreview() {
    ErrorColumn(message = "Oopsie!")
}
