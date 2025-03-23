package com.yasinkacmaz.jetflix

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
    ) {
        JetflixApp()
    }
}
