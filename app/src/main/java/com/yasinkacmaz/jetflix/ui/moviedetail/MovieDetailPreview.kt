package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.compose.animation.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.util.randomColor

@Preview
@Composable
private fun MovieDetailPreview() {
    val movieDetail = MovieDetail(
        id = 1337,
        title = "A great movie",
        originalTitle = "Un grand film",
        overview = """
            |Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
            |et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
            |aliquip ex ea commodo consequat.
        """.trimMargin(),
        tagline = "My first movie",
        backdropUrl = "url",
        posterUrl = "url",
        genres = listOf(Genre(1, "Action"), Genre(2, "Comedy"), Genre(3, "Fantasy")),
        releaseDate = "06.12.1994",
        voteAverage = 5.7,
        voteCount = 1337,
        duration = 137,
        productionCompanies = listOf(
            ProductionCompany("Marvel", ""),
            ProductionCompany("Pixar", ""),
            ProductionCompany("Warner Bros", "")
        ),
        homepage = "homepageUrl"
    )
    val person =
        Person(profilePhotoUrl = "", name = "Yasin Kaçmaz", role = "Android Developer", gender = Gender.MALE, id = 1337)
    val people = listOf(person, person, person, person)
    val credits = Credits(cast = people, crew = people)
    val vibrantColor = remember { Animatable(Color.randomColor()) }
    CompositionLocalProvider(LocalVibrantColor provides vibrantColor) {
        val images = listOf(Image("", 1), Image("", 1), Image("", 1))
        MovieDetail(movieDetail = movieDetail, cast = credits.cast, crew = credits.crew, images = images)
    }
}
