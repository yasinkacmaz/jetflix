package com.yasinkacmaz.jetflix

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.compose.rememberNavController
import com.yasinkacmaz.jetflix.di.initializeKoin
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    initializeKoin()
    ComposeViewport(document.body!!) {
        val navController = rememberNavController()
        JetflixApp(startScreen = Screen.Splash, navController = navController)
        LaunchedEffect(navController) {
            navController.bindToBrowserNavigation()
        }
    }
}
