package com.yasinkacmaz.playground.ui.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.staticAmbientOf
import androidx.lifecycle.LifecycleOwner

val NavigatorAmbient = staticAmbientOf<Navigator<Screen>> { error("No navigator instance available") }

class Navigator<T : Screen>(
    initialScreen: T,
    lifecycleOwner: LifecycleOwner,
    dispatcher: OnBackPressedDispatcher
) {
    private val stack = mutableStateListOf(initialScreen)
    private val callback = object : OnBackPressedCallback(canGoBack()) {
        override fun handleOnBackPressed() {
            goBack()
        }
    }.also { callback ->
        dispatcher.addCallback(lifecycleOwner, callback)
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
