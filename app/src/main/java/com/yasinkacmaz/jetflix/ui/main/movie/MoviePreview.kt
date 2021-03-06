package com.yasinkacmaz.jetflix.ui.main.movie

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

@Preview(name = "Preview", group = "Size")
@Preview(name = "At Activity", showSystemUi = true, group = "Devices")
@Preview(name = "Pixel 4XL", showSystemUi = true, device = Devices.PIXEL_4_XL, group = "Devices")
@Preview(name = "Wide", widthDp = 666, group = "Size")
@Composable
private fun MovieMultiPreview() {
    MoviePreview {
        MovieContent(fakeMovie)
    }
}

@Composable
fun MoviePreview(content: @Composable () -> Unit) {
    JetflixTheme(content = content)
}
