package com.yasinkacmaz.jetflix.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

context(ViewModel)
fun Dispatchers.onMain(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(main, block = block)

context(ViewModel)
fun Dispatchers.onIO(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(io, block = block)
