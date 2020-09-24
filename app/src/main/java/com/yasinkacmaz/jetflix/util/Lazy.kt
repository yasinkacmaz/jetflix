package com.yasinkacmaz.jetflix.util

import kotlin.LazyThreadSafetyMode.NONE

fun <T : Any> unsafeLazy(initializer: () -> T) = lazy(NONE, initializer)
