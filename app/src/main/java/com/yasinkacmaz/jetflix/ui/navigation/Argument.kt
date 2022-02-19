package com.yasinkacmaz.jetflix.ui.navigation

inline val String.argumentCount: Int get() = arguments().count()

@Suppress("RegExpRedundantEscape")
fun String.arguments(): Sequence<MatchResult> {
    val argumentRegex = "\\{(.*?)\\}".toRegex() // {...}
    return argumentRegex.findAll(this)
}
