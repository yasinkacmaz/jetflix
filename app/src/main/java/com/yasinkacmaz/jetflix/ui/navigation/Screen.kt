package com.yasinkacmaz.jetflix.ui.navigation

import com.yasinkacmaz.jetflix.data.Genre

sealed class Screen {
    object FetchGenres : Screen()

    data class Genres(val genres: List<Genre>) : Screen()

    data class MovieDetail(val movieId: Int) : Screen()
}
