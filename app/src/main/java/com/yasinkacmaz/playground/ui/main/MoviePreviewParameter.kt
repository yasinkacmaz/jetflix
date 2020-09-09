package com.yasinkacmaz.playground.ui.main

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.PreviewParameter
import androidx.ui.tooling.preview.PreviewParameterProvider
import androidx.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.yasinkacmaz.playground.data.Movie

val fakeMovie = Movie("", "", "", "", "", 9.24, 1337)

class MovieProvider : PreviewParameterProvider<Movie> {
    override val values: Sequence<Movie>
        get() = sequenceOf(
            fakeMovie.copy(name = "Friends"), fakeMovie.copy(name = "Lost")
        )
}

@Composable
@Preview(group = "Series")
fun Series(@PreviewParameter(MovieProvider::class) movie: Movie) {
    MaterialTheme {
        MovieLayout(movie)
    }
}

class MovieCollectionProvider : CollectionPreviewParameterProvider<Movie>(
    listOf(
        fakeMovie.copy(name = "Godfather"), fakeMovie.copy(name = "Harry Potter")
    )
)

@Composable
@Preview(group = "Films")
fun Films(@PreviewParameter(MovieCollectionProvider::class) movie: Movie) {
    MaterialTheme {
        MovieLayout(movie)
    }
}
