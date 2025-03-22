package com.yasinkacmaz.jetflix.ui.settings

import android.annotation.SuppressLint
import java.util.Locale
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    @SerialName("english_name") val englishName: String,
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("name") val name: String,
) {
    companion object {
        @SuppressLint("ConstantLocale")
        val default = Language(
            englishName = Locale.getDefault().displayLanguage,
            iso6391 = Locale.getDefault().language,
            name = Locale.getDefault().displayLanguage,
        )
    }
}

inline val Language.flagUrl get() = "https://www.unknown.nu/flags/images/$iso6391-100"
inline val Language.displayName get() = name.ifBlank { englishName }
