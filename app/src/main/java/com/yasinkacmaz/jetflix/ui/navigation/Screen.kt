package com.yasinkacmaz.jetflix.ui.navigation

import com.yasinkacmaz.jetflix.data.Genre

sealed class Screen {
    object FetchGenresScreen : Screen()

    data class GenresScreen(val genres: List<Genre>) : Screen()

    data class MovieDetailScreen(val movieId: Int) : Screen()
}
