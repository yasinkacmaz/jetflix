package com.yasinkacmaz.jetflix.movie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieItem
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class MovieContentTest {

    @Test
    fun should_render_basic_movie_information() = runComposeUiTest {
        val movie = Movie(
            id = 1337,
            name = "Movie Name",
            releaseDate = "01.03.1337",
            posterPath = "",
            voteAverage = 9.24,
            voteCount = 1337,
        )

        setTestContent {
            MovieItem(movie, Modifier.fillMaxSize(0.5f))
        }

        onNodeWithText(movie.name, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.releaseDate, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.voteCount.toString(), useUnmergedTree = true).assertIsDisplayed()
        onNodeWithText(movie.voteAverage.toString(), useUnmergedTree = true).assertIsDisplayed()
    }
}
