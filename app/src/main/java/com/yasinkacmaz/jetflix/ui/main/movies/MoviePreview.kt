package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.main.genres.SelectedGenreAmbient
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

@Preview(name = "Preview", group = "Size")
@Preview(name = "At Activity", showDecoration = true, group = "Devices")
@Preview(name = "Pixel 4XL", showDecoration = true, device = Devices.PIXEL_4_XL, group = "Devices")
@Preview(name = "Wide", widthDp = 666, group = "Size")
@Composable
private fun MovieMultiPreview() {
    MoviePreview {
        MovieContent(fakeMovie)
    }
}

@Composable
fun MoviePreview(content: @Composable () -> Unit) {
    Providers(SelectedGenreAmbient provides mutableStateOf(GenreUiModel(Genre(-1, "Genre")))) {
        JetflixTheme(content = content)
    }
}
