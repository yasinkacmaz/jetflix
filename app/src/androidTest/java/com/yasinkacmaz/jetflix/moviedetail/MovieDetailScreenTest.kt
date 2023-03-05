package com.yasinkacmaz.jetflix.moviedetail

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.ui.moviedetail.LocalVibrantColor
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetail
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.randomColor
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Rule
import org.junit.Test

class MovieDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val movieDetail = MovieDetail(
        id = 1,
        releaseDate = "01.03.1337",
        duration = 137,
        voteAverage = 7.3,
        voteCount = 1337,
        genres = listOf(Genre(1, "Action"), Genre(2, "Drama"), Genre(3, "Family")),
        tagline = "Tagline",
        overview = "Overview",
    )

    @Test
    fun should_not_render_original_title_if_same_with_name(): Unit = with(composeTestRule) {
        val title = "Title"
        val originalTitle = "Title"

        renderMovieDetail(movieDetail.copy(title = title, originalTitle = originalTitle))

        onAllNodesWithText(title, useUnmergedTree = false).assertCountEquals(1)
    }

    @Test
    fun should_render_original_title_with_parentheses_if_different_from_name(): Unit = with(composeTestRule) {
        val title = "Title"
        val originalTitle = "Original Title"

        renderMovieDetail(movieDetail.copy(title = title, originalTitle = originalTitle))

        onNodeWithText(title, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("($originalTitle)", useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun should_render_movie_info(): Unit = with(composeTestRule) {
        val person =
            Person(name = "", role = "", profilePhotoUrl = "https://t.ly/24r5", gender = Gender.FEMALE, id = 1337)
        val credits = Credits(
            cast = listOf(
                person.copy("Scarlett Johansson", "Natasha Romanoff", gender = Gender.FEMALE),
                person.copy("Stan Lee", "Characters", gender = Gender.MALE),
                person.copy("Al Pacino", "Tony Montana", gender = Gender.MALE),
            ),
            crew = listOf(
                person.copy("Quentin Tarantino", "Director", gender = Gender.MALE),
                person.copy("J.K. Rowling", "Novel", gender = Gender.FEMALE),
                person.copy("Hans Zimmer", "Music Composer", gender = Gender.MALE),
            ),
        )
        renderMovieDetail(movieDetail, credits)

        onNodeWithText(movieDetail.releaseDate, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("${movieDetail.duration} min", useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.voteAverage.toString(), useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.voteCount.toString(), useUnmergedTree = false).assertIsDisplayed()
        movieDetail.genres.forEach { onNodeWithText(it.name!!, useUnmergedTree = false).assertIsDisplayed() }
        onNodeWithText(movieDetail.tagline).performScrollTo()
        onNodeWithText(movieDetail.tagline, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.overview).performScrollTo()
        onNodeWithText(movieDetail.overview, useUnmergedTree = false).assertIsDisplayed()
        assertPeople(R.string.cast, credits.cast)
        assertPeople(R.string.crew, credits.crew)
    }

    private fun ComposeContentTestRule.assertPeople(@StringRes tagResId: Int, people: List<Person>) {
        val peopleLazyRow = onNodeWithTag(composeTestRule.getString(tagResId)).performScrollTo()
        people.forEachIndexed { index, person ->
            peopleLazyRow.performScrollToIndex(index)
            onNodeWithText(person.name, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
            onNodeWithText(person.role, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        }
    }

    private fun ComposeContentTestRule.renderMovieDetail(
        movieDetail: MovieDetail,
        credits: Credits = Credits(emptyList(), emptyList()),
    ) = setTestContent {
        val dominantColor = remember(movieDetail.id) { Animatable(Color.randomColor()) }
        CompositionLocalProvider(LocalVibrantColor provides dominantColor) {
            MovieDetail(movieDetail, credits.cast, credits.crew, listOf())
        }
    }
}
