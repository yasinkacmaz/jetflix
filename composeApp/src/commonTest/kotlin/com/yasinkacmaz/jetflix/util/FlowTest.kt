package com.yasinkacmaz.jetflix.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.test(): List<T> {
    val values = mutableListOf<T>()
    this
        .onEach(values::add)
        .launchIn(CoroutineScope(testAppDispatchers.main))
    return values
}
