package com.yasinkacmaz.jetflix.util

fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342${this}"

fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/original${this}"
