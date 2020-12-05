package com.yasinkacmaz.jetflix.util

@OptIn(ExperimentalStdlibApi::class)
fun <T : Any> List<T>.toPairs(): List<Pair<T, T>> = buildList {
    for (index in this@toPairs.indices.step(2)) {
        add(this@toPairs[index] to this@toPairs[index + 1])
    }
}
