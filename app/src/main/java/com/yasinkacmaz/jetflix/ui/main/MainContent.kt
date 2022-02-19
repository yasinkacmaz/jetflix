package com.yasinkacmaz.jetflix.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailViewModel
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImagesScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.person.PeopleGridScreen
import com.yasinkacmaz.jetflix.ui.movies.MoviesScreen
import com.yasinkacmaz.jetflix.ui.navigation.ARG_INITIAL_PAGE
import com.yasinkacmaz.jetflix.ui.navigation.ARG_MOVIE_ID
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.settings.SettingsContent

val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }

@Composable
fun MainContent(
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>
) {
    val navController = LocalNavController.current
    NavHost(navController = navController, startDestination = Screen.MOVIES.route) {
        composable(Screen.MOVIES.route) { MoviesScreen(isDarkTheme, showSettingsDialog) }

        navigation(startDestination = Screen.DETAIL.route, route = "movie") {
            argument(ARG_MOVIE_ID) { type = NavType.StringType }
            val movieId = { navController.currentBackStackEntry?.arguments?.getString(ARG_MOVIE_ID)!!.toInt() }

            val movieDetailViewModel: @Composable (movieId: Int) -> MovieDetailViewModel = { hiltViewModel() }

            composable(route = Screen.DETAIL.route) {
                MovieDetailScreen(movieDetailViewModel(movieId()))
            }

            composable(
                route = Screen.IMAGES.route,
                arguments = listOf(navArgument(ARG_INITIAL_PAGE) { defaultValue = "0" })
            ) {
                val initialPage = it.arguments?.getString(ARG_INITIAL_PAGE)!!.toInt()
                val images = movieDetailViewModel(movieId()).uiValue.images
                ImagesScreen(images, initialPage)
            }

            composable(route = Screen.CAST.route) {
                val cast = movieDetailViewModel(movieId()).uiValue.credits.cast
                PeopleGridScreen(cast)
            }

            composable(route = Screen.CREW.route) {
                val crew = movieDetailViewModel(movieId()).uiValue.credits.crew
                PeopleGridScreen(crew)
            }
        }
    }

    if (showSettingsDialog.value) {
        SettingsContent {
            showSettingsDialog.value = false
        }
    }
}
