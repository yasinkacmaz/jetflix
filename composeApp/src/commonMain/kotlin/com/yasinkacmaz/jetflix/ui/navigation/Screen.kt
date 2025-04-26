package com.yasinkacmaz.jetflix.ui.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    @SerialName("splash")
    data object Splash : Screen()

    @Serializable
    @SerialName("movies")
    data object Movies : Screen()

    @Serializable
    @SerialName("movie")
    data class MovieDetail(val movieId: Int) : Screen()

    @Serializable
    @SerialName("images")
    data class MovieImages(val movieId: Int, val initialPage: Int) : Screen()

    @Serializable
    @SerialName("cast")
    data class MovieCast(val movieId: Int) : Screen()

    @Serializable
    @SerialName("crew")
    data class MovieCrew(val movieId: Int) : Screen()

    @Serializable
    @SerialName("profile")
    data class Profile(val personId: Int) : Screen()

    @Serializable
    @SerialName("favorites")
    data object Favorites : Screen()
}
