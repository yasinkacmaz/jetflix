package com.yasinkacmaz.jetflix.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        renderUi()
        settingsViewModel.onSettingsChanged.observe(this) { restart() }
    }

    private fun renderUi() = setContent {
        val showSettingsDialog = remember { mutableStateOf(false) }
        val systemTheme = isSystemInDarkTheme()
        val isDarkTheme = remember { mutableStateOf(systemTheme) }
        val navController = rememberNavController()
        JetflixTheme(isDarkTheme = isDarkTheme.value) {
            CompositionLocalProvider(LocalNavController provides navController) {
                MainContent(isDarkTheme, showSettingsDialog)
            }
        }
    }

    private fun restart() {
        finish()
        startActivity(intent)
    }
}
