package com.yasinkacmaz.jetflix.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.ui.test.createComposeRule
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.main.genres.SelectedGenreAmbient
import com.yasinkacmaz.jetflix.ui.main.movies.Movie
import com.yasinkacmaz.jetflix.ui.main.movies.MovieContent
import org.junit.Rule
import org.junit.Test

class MovieContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val TAG = "MovieContentTest"
    private val movie = Movie(
        id = 1337,
        name = "Movie Name",
        originalName = "",
        overview = "",
        releaseDate = "01.03.1337",
        posterPath = "",
        voteAverage = 9.24,
        voteCount = 1337
    )

    @Test
    fun `Should not find movie information with merged tree`(): Unit = with(composeTestRule) {
        setMovieContent()

        onRoot().printToLog(TAG)
        onNodeWithText("Movie Name", useUnmergedTree = false).assertDoesNotExist()
        onNodeWithText("01.03.1337", useUnmergedTree = false).assertDoesNotExist()
        onNodeWithText(1337.toString(), useUnmergedTree = false).assertDoesNotExist()
    }

    @Test
    fun `Should assert movie information with unmerged tree`(): Unit = with(composeTestRule) {
        setMovieContent()

        onRoot().printToLog(TAG)
        onNodeWithText("Movie Name", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText("01.03.1337", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(1337.toString(), useUnmergedTree = true).assertIsDisplayed()
    }

    private fun ComposeTestRule.setMovieContent() = setContent {
        Providers(SelectedGenreAmbient provides mutableStateOf(GenreUiModel(Genre(-1, "Genre")))) {
            Box(Modifier.fillMaxSize()) {
                MovieContent(movie)
            }
        }
    }
}
