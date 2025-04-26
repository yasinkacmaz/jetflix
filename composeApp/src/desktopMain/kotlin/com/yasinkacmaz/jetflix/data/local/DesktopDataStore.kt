package com.yasinkacmaz.jetflix.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DesktopDataStore(private val preferences: DataStore<Preferences>) : LocalDataStore {

    override fun get(key: String): Flow<String?> = preferences.data.map { it[stringPreferencesKey(key)] }

    override suspend fun set(key: String, newValue: String) {
        preferences.edit { it[stringPreferencesKey(key)] = newValue }
    }
}
