package com.yasinkacmaz.jetflix.ui.main.settings

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.util.Locale

data class Language(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("name") val name: String
) {
    companion object {
        @SuppressLint("ConstantLocale")
        val default = Language(
            englishName = Locale.getDefault().displayLanguage,
            iso6391 = Locale.getDefault().language,
            name = Locale.getDefault().displayLanguage
        )
    }
}

inline val Language.flagUrl get() = "https://www.unknown.nu/flags/images/$iso6391-100"
