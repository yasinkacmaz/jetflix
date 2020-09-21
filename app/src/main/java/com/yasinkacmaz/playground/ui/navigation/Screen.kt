package com.yasinkacmaz.playground.ui.navigation

import com.yasinkacmaz.playground.data.Genre

sealed class Screen {
    object FetchGenresScreen : Screen()

    data class GenresScreen(val genres: List<Genre>) : Screen()

    data class MovieDetailScreen(val movieId: String) : Screen()
}
