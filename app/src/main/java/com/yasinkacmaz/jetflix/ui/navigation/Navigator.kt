package com.yasinkacmaz.jetflix.ui.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.LifecycleOwner

val LocalNavigator = staticCompositionLocalOf<Navigator<Screen>> { error("No navigator instance available") }

class Navigator<T : Screen>(
    initialScreen: T,
    lifecycleOwner: LifecycleOwner? = null,
    dispatcher: OnBackPressedDispatcher? = null
) {
    private val stack = mutableStateListOf(initialScreen)
    private val callback = object : OnBackPressedCallback(canGoBack()) {
        override fun handleOnBackPressed() {
            goBack()
        }
    }.also { callback ->
        if (dispatcher != null && lifecycleOwner != null) {
            dispatcher.addCallback(lifecycleOwner, callback)
        }
    }

    private fun canGoBack() = stack.size > 1

    val currentScreen get() = stack.last()

    fun goBack() {
        stack.removeLast()
        callback.isEnabled = canGoBack()
    }

    fun navigateTo(screen: T, keepCurrentScreen: Boolean = true) {
        if (keepCurrentScreen.not()) {
            stack.removeLast()
        }
        stack += screen
        callback.isEnabled = canGoBack()
    }
}
