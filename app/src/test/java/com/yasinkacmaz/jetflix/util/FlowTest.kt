package com.yasinkacmaz.jetflix.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestScope

context(TestScope)
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.test(): List<T> {
    val values = mutableListOf<T>()
    this
        .onEach(values::add)
        .launchIn(CoroutineScope(testDispatchers.main))
    return values
}
