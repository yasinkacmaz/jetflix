package com.yasinkacmaz.playground.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.platform.setContent
import com.yasinkacmaz.playground.ui.main.genres.FetchGenresContent
import com.yasinkacmaz.playground.ui.main.genres.GenresContent
import com.yasinkacmaz.playground.ui.main.moviedetail.MovieDetailContent
import com.yasinkacmaz.playground.ui.navigation.Navigator
import com.yasinkacmaz.playground.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.playground.ui.navigation.Screen
import com.yasinkacmaz.playground.ui.navigation.Screen.FetchGenresScreen
import com.yasinkacmaz.playground.ui.navigation.Screen.GenresScreen
import com.yasinkacmaz.playground.ui.navigation.Screen.MovieDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderUi()
    }

    private fun renderUi() {
        setContent {
            val lifecycleOwner = LifecycleOwnerAmbient.current
            val navigator = remember {
                Navigator<Screen>(FetchGenresScreen, lifecycleOwner, onBackPressedDispatcher)
            }
            MainContent(navigator)
        }
    }

    @Composable
    private fun MainContent(navigator: Navigator<Screen>) {
        Providers(NavigatorAmbient provides navigator) {
            Crossfade(current = navigator.currentScreen) { screen ->
                when (screen) {
                    FetchGenresScreen -> FetchGenresContent()
                    is GenresScreen -> GenresContent(screen.genres)
                    is MovieDetailScreen -> MovieDetailContent()
                }
            }
        }
    }
}
