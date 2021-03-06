package com.yasinkacmaz.jetflix.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.main.movie.Movie
import com.yasinkacmaz.jetflix.ui.main.movie.MovieContent
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Rule
import org.junit.Test

class MovieContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val movie = Movie(
        id = 1337,
        name = "Movie Name",
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

    private fun ComposeContentTestRule.setMovieContent() = setTestContent {
        Box(
            Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            MovieContent(movie)
        }
    }

    companion object {
        private const val TAG = "MovieContentTest"
    }
}
