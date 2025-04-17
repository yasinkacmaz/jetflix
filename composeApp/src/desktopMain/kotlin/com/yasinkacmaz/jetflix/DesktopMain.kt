package com.yasinkacmaz.jetflix

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.yasinkacmaz.jetflix.di.initializeKoin
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    initializeKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
    ) {
        JetflixApp()
    }
}
