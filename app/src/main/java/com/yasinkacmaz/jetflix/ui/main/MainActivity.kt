package com.yasinkacmaz.jetflix.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Navigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        renderUi()
        settingsViewModel.onSettingsChanged.observe(this) {
            finish()
            startActivity(intent)
        }
    }

    private fun renderUi() {
        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val navigator = remember { Navigator<Screen>(Screen.Movies, lifecycleOwner, onBackPressedDispatcher) }
            val showSettingsDialog = remember { mutableStateOf(false) }
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = remember { mutableStateOf(systemTheme) }
            JetflixTheme(isDarkTheme = isDarkTheme.value) {
                ProvideWindowInsets {
                    CompositionLocalProvider(LocalNavigator provides navigator) {
                        MainContent(navigator, isDarkTheme, showSettingsDialog)
                    }
                }
            }
        }
    }
}
