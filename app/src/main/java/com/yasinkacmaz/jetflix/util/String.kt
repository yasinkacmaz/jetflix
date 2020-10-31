package com.yasinkacmaz.jetflix.util

fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342$this"

fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/original$this"

fun String.toProfilePhotoUrl() = "https://image.tmdb.org/t/p/w185$this"

fun String.toOriginalUrl() = "https://image.tmdb.org/t/p/original$this"
