package com.yasinkacmaz.jetflix.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.datetime.LocalDate

fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342$this"

fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/original$this"
fun String.toProfilePhotoUrl() = "https://image.tmdb.org/t/p/w185$this"

fun String.toOriginalUrl() = "https://image.tmdb.org/t/p/original$this"

fun String.toImdbProfileUrl() = "https://www.imdb.com/name/$this"

fun String.openInChromeCustomTab(context: Context, color: Color) {
    val schemeParams = CustomTabColorSchemeParams.Builder().setToolbarColor(color.toArgb()).build()
    val customTabsIntent = CustomTabsIntent.Builder().setDefaultColorSchemeParams(schemeParams).build()
    customTabsIntent.launchUrl(context, Uri.parse(this))
}

fun String?.parseAsDate(): String {
    if (this == null) return ""

    val parsed = try {
        LocalDate.parse(this)
    } catch (exception: Exception) {
        return ""
    }

    fun Int.asTwoDigits(): String = this.toString().padStart(2, '0')
    return "${parsed.dayOfMonth.asTwoDigits()}.${parsed.monthNumber.asTwoDigits()}.${parsed.year.asTwoDigits()}"
}
