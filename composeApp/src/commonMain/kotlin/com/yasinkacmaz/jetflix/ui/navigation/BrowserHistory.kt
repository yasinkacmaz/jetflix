package com.yasinkacmaz.jetflix.ui.navigation

import androidx.compose.runtime.Composable

expect class BrowserHistory() {
    @Composable
    fun Sync(backStack: MutableList<Screen>, navigator: JetflixNavigator)
}
