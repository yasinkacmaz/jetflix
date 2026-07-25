package com.yasinkacmaz.jetflix

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.yasinkacmaz.jetflix.di.initializeKoin
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initializeKoin()
    ComposeViewport(document.body!!) {
        JetflixApp(startScreen = Screen.Splash)
    }
}
