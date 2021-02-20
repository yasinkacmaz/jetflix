package com.yasinkacmaz.jetflix.ui.main.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Providers
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.ui.main.genres.LocalSelectedGenre
import com.yasinkacmaz.jetflix.ui.main.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.navigation.Screen.FetchGenres
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
        this.setContent(null) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val navigator = remember { Navigator<Screen>(FetchGenres, lifecycleOwner, onBackPressedDispatcher) }
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = remember { mutableStateOf(systemTheme) }
            val showSettingsDialog = remember { mutableStateOf(false) }
            val selectedGenre = remember { mutableStateOf(GenreUiModel()) }
            Providers(LocalNavigator provides navigator, LocalSelectedGenre provides selectedGenre) {
                ProvideWindowInsets {
                    JetflixTheme(isDarkTheme = isDarkTheme.value) {
                        MainContent(navigator, isDarkTheme, showSettingsDialog)
                    }
                }
            }
        }
    }
}
