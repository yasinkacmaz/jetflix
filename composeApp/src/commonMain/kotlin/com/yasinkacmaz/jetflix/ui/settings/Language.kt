package com.yasinkacmaz.jetflix.ui.settings

import androidx.compose.ui.text.intl.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    @SerialName("english_name") val englishName: String,
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("name") val name: String,
) {
    companion object {
        val default = Language(
            englishName = "System default",
            iso6391 = Locale.current.language,
            name = "System default",
        )
    }
}

inline val Language.flagUrl get() = "https://www.unknown.nu/flags/images/$iso6391-100"
inline val Language.displayName get() = name.ifBlank { englishName }
