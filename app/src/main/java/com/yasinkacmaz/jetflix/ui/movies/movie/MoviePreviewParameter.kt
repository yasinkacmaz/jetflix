package com.yasinkacmaz.jetflix.ui.movies.movie

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

val fakeMovie = Movie(1337, "Movie Name", "01.03.1337", "Poster", 9.24, 1337)

class MovieProvider : PreviewParameterProvider<Movie> {
    override val values: Sequence<Movie>
        get() = sequenceOf(fakeMovie.copy(name = "Friends"), fakeMovie.copy(name = "Lost"))
}

@Composable
@Preview(group = "Series")
private fun Series(@PreviewParameter(MovieProvider::class) movie: Movie) {
    MoviePreview {
        MovieContent(movie)
    }
}

class MovieCollectionProvider : CollectionPreviewParameterProvider<Movie>(
    listOf(fakeMovie.copy(name = "Godfather"), fakeMovie.copy(name = "Harry Potter")),
)

@Composable
@Preview(group = "Films")
private fun Films(@PreviewParameter(MovieCollectionProvider::class) movie: Movie) {
    MoviePreview {
        MovieContent(movie)
    }
}
