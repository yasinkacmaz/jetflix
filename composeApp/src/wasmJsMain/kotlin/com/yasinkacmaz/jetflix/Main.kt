package com.yasinkacmaz.jetflix

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import androidx.navigation.compose.rememberNavController
import com.yasinkacmaz.jetflix.di.initializeKoin
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    initializeKoin()
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        val navController = rememberNavController()
        JetflixApp(startScreen = Screen.Splash, navController = navController)
        LaunchedEffect(Unit) {
            window.bindToNavigation(navController)
        }
    }
}
