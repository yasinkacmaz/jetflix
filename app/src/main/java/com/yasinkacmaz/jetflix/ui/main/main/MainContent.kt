package com.yasinkacmaz.jetflix.ui.main.main

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.yasinkacmaz.jetflix.ui.main.images.ImagesScreen
import com.yasinkacmaz.jetflix.ui.main.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.main.moviedetail.person.PeopleGridScreen
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesScreen
import com.yasinkacmaz.jetflix.ui.main.settings.SettingsContent
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen

@Composable
fun MainContent(
    navigator: Navigator<Screen>,
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>
) {
    Crossfade(targetState = navigator.currentScreen) { screen ->
        when (screen) {
            is Screen.Movies -> MoviesScreen(isDarkTheme, showSettingsDialog)
            is Screen.MovieDetail -> MovieDetailScreen(screen.movieId)
            is Screen.Images -> ImagesScreen(screen.images)
            is Screen.PeopleGrid -> PeopleGridScreen(screen.people)
        }
    }

    if (showSettingsDialog.value) {
        SettingsContent {
            showSettingsDialog.value = false
        }
    }
}
