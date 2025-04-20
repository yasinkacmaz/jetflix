package com.yasinkacmaz.jetflix

import androidx.compose.ui.window.ComposeUIViewController
import com.yasinkacmaz.jetflix.ui.navigation.Screen

fun mainViewController() = ComposeUIViewController { JetflixApp(startScreen = Screen.Splash) }
