package com.yasinkacmaz.jetflix

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.createComposeRule
import androidx.ui.test.onNodeWithText
import androidx.ui.test.onRoot
import androidx.ui.test.printToLog
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
    fun `Should Not Find Movie Information With Merged Tree`(): Unit = with(composeTestRule) {
        setContent {
            Providers(SelectedGenreAmbient provides mutableStateOf(GenreUiModel(Genre(-1, "Genre")))) {
                Box(Modifier.fillMaxSize()) {
                    MovieContent(movie)
                }
            }
        }

        onRoot().printToLog(TAG)
        onNodeWithText("Movie Name", ignoreCase = false, useUnmergedTree = false).assertDoesNotExist()
        onNodeWithText("01.03.1337", ignoreCase = false, useUnmergedTree = false).assertDoesNotExist()
        onNodeWithText(1337.toString(), ignoreCase = false, useUnmergedTree = false).assertDoesNotExist()
    }

    @Test
    fun `Should Assert Movie Information With Unmerged Tree`(): Unit = with(composeTestRule) {
        setContent {
            Providers(SelectedGenreAmbient provides mutableStateOf(GenreUiModel(Genre(-1, "Genre")))) {
                Box(Modifier.fillMaxSize()) {
                    MovieContent(movie)
                }
            }
        }

        onRoot().printToLog(TAG)
        onNodeWithText("Movie Name", ignoreCase = false, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText("01.03.1337", ignoreCase = false, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(1337.toString(), ignoreCase = false, useUnmergedTree = true).assertIsDisplayed()
    }
}
