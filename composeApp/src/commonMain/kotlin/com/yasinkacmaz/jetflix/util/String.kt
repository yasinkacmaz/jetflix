package com.yasinkacmaz.jetflix.util

import androidx.compose.ui.platform.UriHandler
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

fun String.toSmallPosterUrl() = "https://image.tmdb.org/t/p/w185$this"

fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342$this"

fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/w780$this"

fun String.toProfilePhotoUrl() = "https://image.tmdb.org/t/p/w185$this"

fun String.toOriginalUrl() = "https://image.tmdb.org/t/p/w780$this"

fun String.toImdbProfileUrl() = "https://www.imdb.com/name/$this"

fun String.openInBrowser(uriHandler: UriHandler) {
    try {
        uriHandler.openUri(this)
    } catch (_: IllegalArgumentException) {
    }
}

fun String?.parseAsDate(): String {
    if (this.isNullOrBlank()) return ""

    val parsed = try {
        LocalDate.parse(this)
    } catch (_: Exception) {
        return ""
    }

    fun Int.asTwoDigits(): String = this.toString().padStart(2, '0')
    return "${parsed.day.asTwoDigits()}.${parsed.month.number.asTwoDigits()}.${parsed.year.asTwoDigits()}"
}
