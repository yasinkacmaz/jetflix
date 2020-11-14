package com.yasinkacmaz.jetflix

import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.milliseconds
import androidx.ui.test.ComposeTestRuleJUnit
import androidx.ui.test.assertCountEquals
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.center
import androidx.ui.test.createComposeRule
import androidx.ui.test.onAllNodesWithText
import androidx.ui.test.onNodeWithTag
import androidx.ui.test.onNodeWithText
import androidx.ui.test.performGesture
import androidx.ui.test.performScrollTo
import androidx.ui.test.swipe
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.moviedetail.DominantColorAmbient
import com.yasinkacmaz.jetflix.ui.main.moviedetail.MovieDetail
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.util.randomColor
import org.junit.Rule
import org.junit.Test

class MovieDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val movieDetail = MovieDetail(id = 1)

    @Test
    fun `Should Not Render Original Title If Same With Name`(): Unit = with(composeTestRule) {
        val title = "Title"
        val originalTitle = "Title"
        setMovieDetailContent(movieDetail.copy(title = title, originalTitle = originalTitle))

        onAllNodesWithText(title, ignoreCase = false, useUnmergedTree = false).assertCountEquals(1)
    }

    @Test
    fun `Should Render Original Title With Parentheses If Different From Name`(): Unit = with(composeTestRule) {
        val title = "Title"
        val originalTitle = "Original Title"
        setMovieDetailContent(movieDetail.copy(title = title, originalTitle = originalTitle))

        onNodeWithText(title, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("($originalTitle)", ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun `Should Render Genre Chips`(): Unit = with(composeTestRule) {
        val genres = listOf(Genre(1, "Action"), Genre(2, "Drama"), Genre(3, "Family"))
        setMovieDetailContent(movieDetail.copy(genres = genres))

        onNodeWithText("Action", ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("Drama", ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("Family", ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun `Should Render Movie Fields`(): Unit = with(composeTestRule) {
        val releaseDate = "01.03.1337"
        val duration = 137
        val voteAverage = 7.3
        val voteCount = 1337

        setMovieDetailContent(
            movieDetail.copy(
                releaseDate = releaseDate,
                duration = duration,
                voteAverage = voteAverage,
                voteCount = voteCount
            )
        )

        onNodeWithText(releaseDate, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText("$duration min", ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(voteAverage.toString(), ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(voteCount.toString(), ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun `Should Render Tagline And Overview`(): Unit = with(composeTestRule) {
        val tagline = "Tagline"
        val overview = "Overview"
        setMovieDetailContent(
            movieDetail.copy(tagline = tagline, overview = overview)
        )

        onNodeWithText(tagline, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        onNodeWithText(overview, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
    }

    @Test
    fun `Should Render Cast`(): Unit = with(composeTestRule) {
        val tony = Person("Al Pacino", "Tony Montana", "", Gender.MALE)
        val natasha = Person("Scarlett Johansson", "Natasha Romanoff", "", Gender.FEMALE)
        val hermione = Person("Emma Watson", "Hermione Granger", "", Gender.FEMALE)
        val sparrow = Person("Johhny Depp", "Jack Sparrow", "", Gender.MALE)
        val cast = listOf(tony, natasha, hermione, sparrow)
        val credits = Credits(cast = cast, crew = emptyList())
        setMovieDetailContent(movieDetail, credits)

        assertPeople("cast", cast)
    }

    @Test
    fun `Should Render Crew`(): Unit = with(composeTestRule) {
        val klaus = Person("Klaus Badelt", "Composer", "", Gender.MALE)
        val rowling = Person("J.K. Rowling", "Novel", "", Gender.FEMALE)
        val hans = Person("Hans Zimmer", "Music Composer", "", Gender.MALE)
        // stan and quentin is at offscreen. We should scroll them to assert.
        val stan = Person("Stan Lee", "Characters", "", Gender.MALE)
        val quentin = Person("Quentin Tarantino", "Director", "", Gender.MALE)
        val crew = listOf(klaus, rowling, hans, stan, quentin)
        val credits = Credits(cast = emptyList(), crew = crew)
        setMovieDetailContent(movieDetail, credits)

        assertPeople("crew", crew)
    }

    // TODO: Find another way to assert LazyRow items without scrolling them manually.
    //  Using onChildren with LazyRow only returns visible children.
    private fun ComposeTestRuleJUnit.assertPeople(tag: String, people: List<Person>) {
        val peopleLazyRow = onNodeWithTag(tag).performScrollTo()
        people.forEach { person ->
            peopleLazyRow.performGesture {
                val y = center.y
                val node = onNodeWithText(person.name).fetchSemanticsNode()
                val x = node.positionInRoot.x
                val width = node.size.width
                val start = Offset(x + width, y)
                val end = Offset(x, y)
                swipe(start, end, 1000.milliseconds)
                waitForIdle()
            }
            onNodeWithText(person.name, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
            onNodeWithText(person.character, ignoreCase = false, useUnmergedTree = false).assertIsDisplayed()
        }
    }

    private fun ComposeTestRuleJUnit.setMovieDetailContent(
        movieDetail: MovieDetail,
        credits: Credits = Credits(emptyList(), emptyList())
    ) {
        setContent {
            val navigator = remember { Navigator<Screen>(Screen.FetchGenres) }
            val dominantColor = remember(movieDetail.id) { mutableStateOf(Color.randomColor()) }
            Providers(NavigatorAmbient provides navigator, DominantColorAmbient provides dominantColor) {
                MovieDetail(movieDetail, credits, listOf())
            }
        }
    }
}
