package com.yasinkacmaz.jetflix.ui.navigation

import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel

sealed class Screen {
    object FetchGenres : Screen()

    data class Genres(val genreUiModels: List<GenreUiModel>) : Screen()

    data class MovieDetail(val movieId: Int) : Screen()
}
