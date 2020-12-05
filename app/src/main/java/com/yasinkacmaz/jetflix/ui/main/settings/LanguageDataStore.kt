package com.yasinkacmaz.jetflix.ui.main.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageDataStore @Inject constructor(private val gson: Gson, private val preferences: DataStore<Preferences>) {

    val language: Flow<Language> = preferences.data
        .map { preferences ->
            val languageString = preferences[KEY_LANGUAGE]
            if (languageString != null) {
                gson.fromJson(languageString, Language::class.java)
            } else {
                Language.default
            }
        }

    val languageCode: Flow<String> = language.map { it.iso6391 }

    suspend fun onLanguageSelected(language: Language) {
        preferences.edit { preferences ->
            preferences[KEY_LANGUAGE] = gson.toJson(language)
        }
    }

    companion object {
        val KEY_LANGUAGE = preferencesKey<String>("language")
    }
}
