package com.yasinkacmaz.jetflix.ui.navigation

import androidx.compose.runtime.Composable
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation
import com.github.terrakok.navigation3.browser.buildBrowserHistoryFragment

actual class BrowserHistory {
    @Composable
    actual fun Sync(backStack: MutableList<Screen>, navigator: JetflixNavigator) {
        HierarchicalBrowserNavigation {
            when (val screen = backStack.lastOrNull()) {
                is Screen.Splash -> buildBrowserHistoryFragment("splash")
                is Screen.Movies -> buildBrowserHistoryFragment("movies")
                is Screen.MovieDetail -> buildBrowserHistoryFragment(
                    "movie",
                    mapOf("movieId" to screen.movieId.toString()),
                )
                is Screen.MovieImages -> buildBrowserHistoryFragment(
                    "images",
                    mapOf("movieId" to screen.movieId.toString(), "initialPage" to screen.initialPage.toString()),
                )
                is Screen.MovieCast -> buildBrowserHistoryFragment(
                    "cast",
                    mapOf("movieId" to screen.movieId.toString()),
                )
                is Screen.MovieCrew -> buildBrowserHistoryFragment(
                    "crew",
                    mapOf("movieId" to screen.movieId.toString()),
                )
                is Screen.Profile -> buildBrowserHistoryFragment(
                    "profile",
                    mapOf("personId" to screen.personId.toString()),
                )
                is Screen.Favorites -> buildBrowserHistoryFragment("favorites")
                null -> null
            }
        }
    }
}
