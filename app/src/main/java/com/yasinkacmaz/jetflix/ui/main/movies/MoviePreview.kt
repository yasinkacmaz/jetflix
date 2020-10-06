package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview

@Preview(name = "Preview", group = "Size")
@Preview(name = "At Activity", showDecoration = true, group = "Devices")
@Preview(name = "Pixel 4XL", showDecoration = true, device = Devices.PIXEL_4_XL, group = "Devices")
@Preview(name = "Wide", widthDp = 400, group = "Size")
@Composable
fun MovieMultiPreview() {
    MovieContent(fakeMovie)
}
