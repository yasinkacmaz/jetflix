package com.yasinkacmaz.jetflix.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yasinkacmaz.jetflix.ui.theme.spacing
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.try_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun Error(modifier: Modifier = Modifier, message: String, onRetryClicked: (() -> Unit)? = null) {
    Column(
        modifier = modifier.padding(vertical = MaterialTheme.spacing.s),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s, Alignment.CenterVertically),
    ) {
        Text(message, style = MaterialTheme.typography.bodyMedium)
        onRetryClicked?.let {
            Button(onClick = onRetryClicked) {
                Text(stringResource(Res.string.try_again))
            }
        }
    }
}
