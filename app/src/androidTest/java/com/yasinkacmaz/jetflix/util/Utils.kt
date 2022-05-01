package com.yasinkacmaz.jetflix.util

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.getString(@StringRes resId: Int) =
    activity.getString(resId)

fun ComposeContentTestRule.setTestContent(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) = setContent {
    JetflixTheme(isDarkTheme = isDarkTheme) {
        CompositionLocalProvider(LocalNavController provides rememberNavController()) {
            Surface {
                content()
            }
        }
    }
}
