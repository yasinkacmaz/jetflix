package com.yasinkacmaz.jetflix.util

import androidx.compose.runtime.MutableState

fun MutableState<Boolean>.toggle() {
    value = value.not()
}
