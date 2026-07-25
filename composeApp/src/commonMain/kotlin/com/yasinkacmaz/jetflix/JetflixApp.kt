package com.yasinkacmaz.jetflix

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.yasinkacmaz.jetflix.ui.navigation.BrowserHistory
import com.yasinkacmaz.jetflix.ui.navigation.JetflixNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.navigation.SetupNavDisplay
import com.yasinkacmaz.jetflix.ui.navigation.decodeScreen
import com.yasinkacmaz.jetflix.ui.navigation.encodeScreen
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

val LocalNavigator = compositionLocalOf<JetflixNavigator> { error("No navigator") }
val LocalDarkTheme = compositionLocalOf { mutableStateOf(false) }

@Composable
fun JetflixApp(
    startScreen: Screen = Screen.Movies,
    backStack: SnapshotStateList<Screen> = rememberSaveable(
        saver = listSaver<SnapshotStateList<Screen>, String>(
            save = { list -> list.map { encodeScreen(it) } },
            restore = { list -> mutableStateListOf(*list.map { decodeScreen(it) }.toTypedArray()) },
        ),
    ) {
        mutableStateListOf(startScreen)
    },
) {
    val navigator = remember(backStack) { JetflixNavigator(backStack) }
    val browserHistory = remember { BrowserHistory() }
    browserHistory.Sync(backStack, navigator)
    CompositionLocalProvider(
        LocalNavigator provides navigator,
        LocalDarkTheme provides mutableStateOf(isSystemInDarkTheme()),
    ) {
        JetflixTheme(isDarkTheme = LocalDarkTheme.current.value) {
            SetupNavDisplay(backStack = backStack, onBack = { navigator.navigateUp() })
        }
    }
}
