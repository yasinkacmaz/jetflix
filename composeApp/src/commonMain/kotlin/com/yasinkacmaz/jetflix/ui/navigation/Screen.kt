package com.yasinkacmaz.jetflix.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Movies : Screen()

    @Serializable
    data object Movie : Screen()

    @Serializable
    data class MovieDetail(val movieId: Int) : Screen()

    @Serializable
    data class MovieImages(val movieId: Int, val initialPage: Int) : Screen()

    @Serializable
    data class MovieCast(val movieId: Int) : Screen()

    @Serializable
    data class MovieCrew(val movieId: Int) : Screen()

    @Serializable
    data class Profile(val personId: Int) : Screen()

    @Serializable
    data object Favorites : Screen()
}
