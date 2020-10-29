package com.yasinkacmaz.jetflix.ui.navigation

import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image

sealed class Screen {
    object FetchGenres : Screen()

    data class Genres(val genreUiModels: List<GenreUiModel>) : Screen()

    data class MovieDetail(val movieId: Int) : Screen()

    data class Images(val images: List<Image>) : Screen()

    data class PeopleGrid(val people: List<Person>) : Screen()
}
