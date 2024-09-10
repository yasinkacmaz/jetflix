package com.yasinkacmaz.jetflix.ui.movies.movie

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

@Preview(name = "Preview", group = "Size")
@Preview(name = "At Activity", showSystemUi = true, group = "Devices")
@Preview(name = "Pixel 4XL", showSystemUi = true, device = Devices.PIXEL_4_XL, group = "Devices")
@Preview(name = "Wide", widthDp = 666, group = "Size")
@Composable
private fun MovieMultiPreview() {
    MoviePreview {
        MovieItem(fakeMovie)
    }
}

@Composable
fun MoviePreview(content: @Composable () -> Unit) {
    Surface(Modifier.padding(16.dp)) {
        JetflixTheme(content = content)
    }
}
