package com.yasinkacmaz.jetflix.ui.navigation

class JetflixNavigator(private val backStack: MutableList<Screen>) {

    val lastScreen: Screen? get() = backStack.lastOrNull()

    fun navigate(screen: Screen) {
        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
    }

    fun navigateSingleTop(screen: Screen) {
        if (backStack.lastOrNull() != screen) {
            backStack.add(screen)
        }
    }

    fun navigateAndClear(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }

    fun navigateUp(): Boolean {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
            return true
        }
        return false
    }

    fun popUpTo(screen: Screen, inclusive: Boolean = false) {
        val index = backStack.indexOfLast { it == screen }
        if (index >= 0) {
            val removeCount = backStack.size - index - (if (inclusive) 0 else 1)
            repeat(removeCount) { backStack.removeLastOrNull() }
        }
    }

    fun navigateAndPopUpTo(screen: Screen, popUpTo: Screen, inclusive: Boolean = false) {
        popUpTo(popUpTo, inclusive)
        backStack.add(screen)
    }
}
