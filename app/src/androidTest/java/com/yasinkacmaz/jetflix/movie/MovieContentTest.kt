package com.yasinkacmaz.jetflix.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieContent
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Rule
import org.junit.Test

class MovieContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun should_render_basic_movie_information(): Unit = with(composeTestRule) {
        val movie = Movie(
            id = 1337,
            name = "Movie Name",
            releaseDate = "01.03.1337",
            posterPath = "",
            voteAverage = 9.24,
            voteCount = 1337
        )

        renderMovieContent(movie)

        onNodeWithText(movie.name, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.releaseDate, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.voteCount.toString(), useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.voteAverage.toString(), useUnmergedTree = true).assertIsDisplayed()
    }

    private fun ComposeContentTestRule.renderMovieContent(movie: Movie) = setTestContent {
        Box(Modifier.padding(72.dp)) {
            MovieContent(movie)
        }
    }
}
