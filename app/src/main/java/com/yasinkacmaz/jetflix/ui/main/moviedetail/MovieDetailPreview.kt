package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.AmbientNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.util.randomColor

@Preview
@Composable
private fun MovieDetailPreview() {
    val movieDetail = MovieDetail(
        id = 1337,
        title = "A great movie",
        originalTitle = "Un grand film",
        overview = """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt
            ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
            laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in
            voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat
            non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        """,
        tagline = "Enjoy a great movie",
        backdropUrl = "url",
        posterUrl = "url",
        genres = listOf(Genre(1, "Action"), Genre(2, "Comedy")),
        releaseDate = "01.03.1337",
        voteAverage = 5.7,
        voteCount = 1337,
        duration = 137,
        productionCompanies = listOf(ProductionCompany("Marvel", ""), ProductionCompany("Pixar", "")),
        homepage = ""
    )
    val person = Person(profilePhotoUrl = "", name = "Yasin", character = "Android", gender = Gender.MALE)
    val credits = Credits(cast = listOf(person, person, person), crew = listOf(person, person, person))
    val navigator = Navigator<Screen>(Screen.FetchGenres, AmbientLifecycleOwner.current, OnBackPressedDispatcher {})
    Providers(
        AmbientDominantColor provides mutableStateOf(Color.randomColor()),
        AmbientNavigator provides navigator
    ) {
        val images = listOf(
            com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image("", 1),
            com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image("", 1),
            com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image("", 1)
        )
        MovieDetail(movieDetail = movieDetail, credits = credits, images = images)
    }
}
