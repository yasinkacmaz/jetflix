package com.yasinkacmaz.playground.ui.main

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.playground.data.Movie

@Preview(name = "Preview", group = "Size")
@Preview(name = "At Activity", showDecoration = true, group = "Devices")
@Preview(name = "Pixel 4XL", showDecoration = true, device = Devices.PIXEL_4_XL, group = "Devices")
@Preview(name = "Wide", widthDp = 400, group = "Size")
@Composable
fun MoviePreview1() {
    val fakeMovie = Movie(
        "19.07.1997",
        "One Piece",
        "Japanese",
        "A pirate anime",
        "https://image.tmdb.org/t/p/w342/uxFNAo2A6ZRcgNASLk02hJUbybn.jpg",
        9.24,
        1337
    )

    MaterialTheme {
        MovieLayout(fakeMovie)
    }
}
