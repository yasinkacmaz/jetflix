package com.yasinkacmaz.jetflix.moviedetail

import androidx.compose.animation.Animatable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.center
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.moviedetail.LocalVibrantColor
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetail
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.util.randomColor
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Rule
import org.junit.Test

class MovieDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val movieDetail = MovieDetail(id = 1)

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
    fun should_render_genre_chips(): Unit = with(composeTestRule) {
        val genres = listOf(Genre(1, "Action"), Genre(2, "Drama"), Genre(3, "Family"))

        renderMovieDetail(movieDetail.copy(genres = genres))

        onNodeWithText("Action", useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("Drama", useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("Family", useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun should_render_movie_fields(): Unit = with(composeTestRule) {
        val releaseDate = "01.03.1337"
        val duration = 137
        val voteAverage = 7.3
        val voteCount = 1337

        renderMovieDetail(
            movieDetail.copy(
                releaseDate = releaseDate,
                duration = duration,
                voteAverage = voteAverage,
                voteCount = voteCount
            )
        )

        onNodeWithText(releaseDate, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("$duration min", useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(voteAverage.toString(), useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(voteCount.toString(), useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun should_render_tagline_and_overview(): Unit = with(composeTestRule) {
        val tagline = "Tagline"
        val overview = "Overview"

        renderMovieDetail(
            movieDetail.copy(tagline = tagline, overview = overview)
        )

        onNodeWithText(tagline, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(overview, useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun should_render_cast(): Unit = with(composeTestRule) {
        val tony = Person("Al Pacino", "Tony Montana", "", Gender.MALE)
        val natasha = Person("Scarlett Johansson", "Natasha Romanoff", "", Gender.FEMALE)
        val hermione = Person("Emma Watson", "Hermione Granger", "", Gender.FEMALE)
        val sparrow = Person("Johnny Depp", "Jack Sparrow", "", Gender.MALE)
        val cast = listOf(tony, natasha, hermione, sparrow)
        val credits = Credits(cast = cast, crew = emptyList())

        renderMovieDetail(movieDetail, credits)

        assertPeople("cast", cast)
    }

    @Test
    fun should_render_crew(): Unit = with(composeTestRule) {
        val klaus = Person("Klaus Badelt", "Composer", "", Gender.MALE)
        val rowling = Person("J.K. Rowling", "Novel", "", Gender.FEMALE)
        val hans = Person("Hans Zimmer", "Music Composer", "", Gender.MALE)
        // Stan and Quentin is at offscreen. We should scroll them to assert.
        val stan = Person("Stan Lee", "Characters", "", Gender.MALE)
        val quentin = Person("Quentin Tarantino", "Director", "", Gender.MALE)
        val crew = listOf(klaus, rowling, hans, stan, quentin)
        val credits = Credits(cast = emptyList(), crew = crew)

        renderMovieDetail(movieDetail, credits)

        assertPeople("crew", crew)
    }

    // TODO: Find another way to assert LazyRow items without scrolling them manually.
    //  Using onChildren with LazyRow only returns visible children.
    private fun ComposeContentTestRule.assertPeople(tag: String, people: List<Person>) {
        val peopleLazyRow = onNodeWithTag(tag).performScrollTo()
        people.forEach { person ->
            peopleLazyRow.performTouchInput {
                val y = center.y
                val node = onNodeWithText(person.name).fetchSemanticsNode()
                val x = node.positionInRoot.x
                val width = node.size.width
                val start = Offset(x + width, y)
                val end = Offset(x, y)
                swipe(start, end, durationMillis = 1000)
            }
            onNodeWithText(person.name, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
            onNodeWithText(person.role, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        }
    }

    private fun ComposeContentTestRule.renderMovieDetail(
        movieDetail: MovieDetail,
        credits: Credits = Credits(emptyList(), emptyList())
    ) = setTestContent {
        val navigator = remember { Navigator<Screen>(Screen.Movies) }
        val dominantColor = remember(movieDetail.id) { Animatable(Color.randomColor()) }
        CompositionLocalProvider(LocalNavigator provides navigator, LocalVibrantColor provides dominantColor) {
            MovieDetail(movieDetail, credits.cast, credits.crew, listOf())
        }
    }
}
