package com.yasinkacmaz.jetflix.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.platform.setContent
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.genres.FetchGenresContent
import com.yasinkacmaz.jetflix.ui.main.genres.GenresContent
import com.yasinkacmaz.jetflix.ui.main.moviedetail.MovieDetailContent
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.navigation.Screen.FetchGenres
import com.yasinkacmaz.jetflix.ui.navigation.Screen.Genres
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetail
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderUi()
    }

    private fun renderUi() {
        setContent {
            val lifecycleOwner = LifecycleOwnerAmbient.current
            val navigator = remember { Navigator<Screen>(FetchGenres, lifecycleOwner, onBackPressedDispatcher) }
            val isDarkTheme = remember { mutableStateOf(false) }
            val selectedGenre = remember { mutableStateOf(Genre(-1, "")) }
            MainContent(navigator, selectedGenre, isDarkTheme)
        }
    }

    @Composable
    private fun MainContent(
        navigator: Navigator<Screen>,
        selectedGenre: MutableState<Genre>,
        isDarkTheme: MutableState<Boolean>
    ) {
        Providers(NavigatorAmbient provides navigator) {
            JetflixTheme(isDarkTheme = isDarkTheme.value) {
                Crossfade(current = navigator.currentScreen) { screen ->
                    when (screen) {
                        FetchGenres -> FetchGenresContent(selectedGenre)
                        is Genres -> GenresContent(screen.genres, selectedGenre, isDarkTheme)
                        is MovieDetail -> MovieDetailContent(screen.movieId)
                    }
                }
            }
        }
    }
}
