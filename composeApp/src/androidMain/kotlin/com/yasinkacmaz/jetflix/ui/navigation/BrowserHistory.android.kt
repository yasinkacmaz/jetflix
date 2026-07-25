package com.yasinkacmaz.jetflix.ui.navigation

import androidx.compose.runtime.Composable

actual class BrowserHistory {
    @Composable
    actual fun Sync(backStack: MutableList<Screen>, navigator: JetflixNavigator) {
        // No-op on Android
    }
}
