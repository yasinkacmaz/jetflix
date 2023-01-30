package com.yasinkacmaz.jetflix.ui.settings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class Language(
    @SerialName("english_name") val englishName: String,
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("name") val name: String
) {
    companion object {
        val default = Locale.ENGLISH.let {
            Language(
                englishName = it.displayLanguage,
                iso6391 = it.language,
                name = it.displayLanguage
            )
        }
    }
}

inline val Language.flagUrl get() = "https://www.unknown.nu/flags/images/$iso6391-100"
