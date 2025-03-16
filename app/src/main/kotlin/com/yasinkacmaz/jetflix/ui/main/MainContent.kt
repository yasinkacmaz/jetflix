package com.yasinkacmaz.jetflix.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.yasinkacmaz.jetflix.ui.favorites.FavoritesScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailViewModel
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImagesScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.person.PeopleGridScreen
import com.yasinkacmaz.jetflix.ui.movies.MoviesScreen
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.profile.ProfileScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }
val LocalDarkTheme = compositionLocalOf { mutableStateOf(false) }

@Composable
private fun movieDetailViewModel(movieId: Int): MovieDetailViewModel =
    koinViewModel(key = movieId.toString()) { parametersOf(movieId) }

@Composable
fun MainContent() {
    NavHost(navController = LocalNavController.current, startDestination = Screen.Movies) {
        composable<Screen.Movies> {
            MoviesScreen(moviesViewModel = koinViewModel(), filterViewModel = koinViewModel())
        }

        composable<Screen.MovieDetail> {
            MovieDetailScreen(movieDetailViewModel(it.toRoute<Screen.MovieDetail>().movieId))
        }

        composable<Screen.MovieImages> {
            val screen = it.toRoute<Screen.MovieImages>()
            val images = movieDetailViewModel(screen.movieId).uiState.value.images
            ImagesScreen(images, screen.initialPage)
        }

        composable<Screen.MovieCast> {
            val cast = movieDetailViewModel(it.toRoute<Screen.MovieCast>().movieId).uiState.value.credits.cast
            PeopleGridScreen(cast)
        }

        composable<Screen.MovieCrew> {
            val crew = movieDetailViewModel(it.toRoute<Screen.MovieCrew>().movieId).uiState.value.credits.crew
            PeopleGridScreen(crew)
        }

        composable<Screen.Profile> {
            ProfileScreen(koinViewModel { parametersOf(it.toRoute<Screen.Profile>().personId) })
        }

        composable<Screen.Favorites> {
            FavoritesScreen(favoritesViewModel = koinViewModel())
        }
    }
}
