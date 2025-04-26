package com.yasinkacmaz.jetflix.uiTest.moviedetail

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.runComposeUiTest
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetail
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.uiTest.util.setTestContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MovieDetailScreenTest {

    private val movieDetail = MovieDetail(
        id = 1,
        releaseDate = "01.03.1337",
        duration = 137,
        voteAverage = 7.3,
        voteCount = 1337,
        genres = listOf("Action", "Drama", "Family"),
        tagline = "Tagline",
        overview = "Overview",
    )

    @Test
    fun `Should not render original title if its same with title`() = runComposeUiTest {
        val title = "Title"
        val originalTitle = "Title"

        setTestContent {
            MovieDetail(
                movieDetail = movieDetail.copy(title = title, originalTitle = originalTitle),
                cast = emptyList(),
                crew = emptyList(),
                images = listOf(),
                isFavorite = false,
                onFavoriteClicked = {},
            )
        }
        onAllNodesWithText(title, useUnmergedTree = false).assertCountEquals(1)
    }

    @Test
    fun `Should render original title with parentheses if its different from title`() = runComposeUiTest {
        val title = "Title"
        val originalTitle = "Original Title"

        setTestContent {
            MovieDetail(
                movieDetail = movieDetail.copy(title = title, originalTitle = originalTitle),
                cast = emptyList(),
                crew = emptyList(),
                images = listOf(),
                isFavorite = false,
                onFavoriteClicked = {},
            )
        }

        onNodeWithText(title, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("($originalTitle)", useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun `Should render movie detail correctly`() = runComposeUiTest {
        val person = Person(name = "", role = "", profilePhotoUrl = null, gender = Gender.FEMALE, id = 1337)
        val credits = Credits(
            cast = listOf(
                person.copy("Scarlett Johansson", "Natasha Romanoff", gender = Gender.FEMALE),
                person.copy("Stan Lee", "Characters", gender = Gender.MALE),
            ),
            crew = listOf(
                person.copy("Quentin Tarantino", "Director", gender = Gender.MALE),
                person.copy("J.K. Rowling", "Novel", gender = Gender.FEMALE),
            ),
        )
        setTestContent {
            MovieDetail(
                movieDetail = movieDetail,
                cast = credits.cast,
                crew = credits.crew,
                images = listOf(),
                isFavorite = false,
                onFavoriteClicked = {},
            )
        }
        onNodeWithText(movieDetail.releaseDate).performScrollTo()
        onNodeWithText(movieDetail.releaseDate, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("${movieDetail.duration} min", useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.voteAverage.toString(), useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.voteCount.toString(), useUnmergedTree = false).assertIsDisplayed()
        movieDetail.genres.forEach { onNodeWithText(it, useUnmergedTree = false).assertIsDisplayed() }
        onNodeWithText(movieDetail.tagline).performScrollTo()
        onNodeWithText(movieDetail.tagline, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(movieDetail.overview).performScrollTo()
        onNodeWithText(movieDetail.overview, useUnmergedTree = false).assertIsDisplayed()
        assertPeople("Cast", credits.cast)
        assertPeople("Crew", credits.crew)
    }

    private fun ComposeUiTest.assertPeople(tag: String, people: List<Person>) {
        val peopleLazyRow = onNodeWithTag(tag).performScrollTo()
        people.forEachIndexed { index, person ->
            peopleLazyRow.performScrollToIndex(index)
            onNodeWithText(person.name, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
            onNodeWithText(person.role, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        }
    }
}
