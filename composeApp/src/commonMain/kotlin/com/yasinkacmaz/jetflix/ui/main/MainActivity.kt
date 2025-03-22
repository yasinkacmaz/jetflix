package com.yasinkacmaz.jetflix.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        renderUi()
    }

    private fun renderUi() = setContent {
        val systemTheme = isSystemInDarkTheme()
        val isDarkTheme = remember { mutableStateOf(systemTheme) }
        val navController = rememberNavController()
        val systemBarStyle = SystemBarStyle.auto(
            lightScrim = Color.TRANSPARENT,
            darkScrim = Color.TRANSPARENT,
            detectDarkMode = { isDarkTheme.value },
        )
        enableEdgeToEdge(statusBarStyle = systemBarStyle, navigationBarStyle = systemBarStyle)
        SetupCoilImageLoader()
        JetflixTheme(isDarkTheme = isDarkTheme.value) {
            KoinContext {
                CompositionLocalProvider(
                    LocalNavController provides navController,
                    LocalDarkTheme provides isDarkTheme,
                ) {
                    MainContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
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
