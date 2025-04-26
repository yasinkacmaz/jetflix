package com.yasinkacmaz.jetflix

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.navigation.SetupNavGraph
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

val LocalNavController = compositionLocalOf<NavHostController> { error("No nav controller") }
val LocalDarkTheme = compositionLocalOf { mutableStateOf(false) }

@Composable
fun JetflixApp(startScreen: Screen, navController: NavHostController = rememberNavController()) {
    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalDarkTheme provides mutableStateOf(isSystemInDarkTheme()),
    ) {
        JetflixTheme(isDarkTheme = LocalDarkTheme.current.value) {
            SetupCoilImageLoader()
            SetupNavGraph(startScreen)
        }
    }
}

@Composable
private fun SetupCoilImageLoader() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .logger(DebugLogger())
            .build()
    }
}
