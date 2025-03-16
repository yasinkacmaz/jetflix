package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class LanguageDataStore(private val json: Json, private val localDataStore: LocalDataStore) {

    val language: Flow<Language> = localDataStore.get(KEY_LANGUAGE)
        .map { languageString ->
            if (languageString.isNullOrBlank()) {
                Language.default
            } else {
                json.decodeFromString<Language>(languageString)
            }
        }

    val languageCode: Flow<String> = language.map { it.iso6391 }

    suspend fun onLanguageSelected(language: Language) {
        localDataStore.set(KEY_LANGUAGE, json.encodeToString(language))
    }

    companion object {
        private const val KEY_LANGUAGE = "language"
    }
}
