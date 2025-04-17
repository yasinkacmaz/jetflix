package com.yasinkacmaz.jetflix.uiTest.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.navigation.compose.rememberNavController
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.setTestContent(content: @Composable BoxScope.() -> Unit) = setContent {
    JetflixTheme {
        CompositionLocalProvider(LocalNavController provides rememberNavController()) {
            Surface(Modifier.fillMaxSize().systemBarsPadding()) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }
}
