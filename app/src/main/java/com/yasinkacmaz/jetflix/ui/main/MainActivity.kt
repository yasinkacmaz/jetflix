package com.yasinkacmaz.jetflix.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.platform.setContent
import androidx.core.view.WindowCompat
import com.yasinkacmaz.jetflix.ui.main.fetchgenres.FetchGenresScreen
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.main.genres.GenresScreen
import com.yasinkacmaz.jetflix.ui.main.genres.SelectedGenreAmbient
import com.yasinkacmaz.jetflix.ui.main.images.ImagesScreen
import com.yasinkacmaz.jetflix.ui.main.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.main.moviedetail.person.PeopleGridScreen
import com.yasinkacmaz.jetflix.ui.main.settings.SettingsContent
import com.yasinkacmaz.jetflix.ui.main.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.AmbientNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.navigation.Screen.FetchGenres
import com.yasinkacmaz.jetflix.ui.navigation.Screen.Genres
import com.yasinkacmaz.jetflix.ui.navigation.Screen.MovieDetail
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import com.yasinkacmaz.jetflix.util.ProvideDisplayInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        renderUi()
        settingsViewModel.onSettingsChanged.observe(this) {
            finish()
            startActivity(intent)
        }
    }

    private fun renderUi() {
        setContent {
            val lifecycleOwner = LifecycleOwnerAmbient.current
            val navigator = remember { Navigator<Screen>(FetchGenres, lifecycleOwner, onBackPressedDispatcher) }
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = remember { mutableStateOf(systemTheme) }
            val showSettingsDialog = remember { mutableStateOf(false) }
            val selectedGenre = remember { mutableStateOf(GenreUiModel()) }
            MainContent(navigator, selectedGenre, isDarkTheme, showSettingsDialog)
        }
    }

    @Composable
    private fun MainContent(
        navigator: Navigator<Screen>,
        genreUiModel: MutableState<GenreUiModel>,
        isDarkTheme: MutableState<Boolean>,
        showSettingsDialog: MutableState<Boolean>
    ) {
        Providers(AmbientNavigator provides navigator, SelectedGenreAmbient provides genreUiModel) {
            ProvideDisplayInsets {
                JetflixTheme(isDarkTheme = isDarkTheme.value) {
                    Crossfade(current = navigator.currentScreen) { screen ->
                        when (screen) {
                            FetchGenres -> FetchGenresScreen()
                            is Genres -> GenresScreen(screen.genreUiModels, isDarkTheme, showSettingsDialog)
                            is MovieDetail -> MovieDetailScreen(screen.movieId)
                            is Screen.Images -> ImagesScreen(screen.images)
                            is Screen.PeopleGrid -> PeopleGridScreen(screen.people)
                        }
                    }
                    if (showSettingsDialog.value) {
                        SettingsContent() {
                            showSettingsDialog.value = false
                        }
                    }
                }
            }
        }
    }
}
