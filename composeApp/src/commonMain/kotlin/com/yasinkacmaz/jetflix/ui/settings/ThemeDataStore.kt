package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

enum class ThemePreference {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK,
}

class ThemeDataStore(private val localDataStore: LocalDataStore) {

    val themePreference: Flow<ThemePreference> = localDataStore.get(KEY_THEME_PREFERENCE)
        .map { preferenceName ->
            preferenceName?.let { runCatching { ThemePreference.valueOf(it) }.getOrNull() }
                ?: ThemePreference.SYSTEM_DEFAULT
        }
        .catch { emit(ThemePreference.SYSTEM_DEFAULT) }

    suspend fun setThemePreference(preference: ThemePreference) {
        localDataStore.set(KEY_THEME_PREFERENCE, preference.name)
    }

    companion object {
        private const val KEY_THEME_PREFERENCE = "theme_preference"
    }
}
