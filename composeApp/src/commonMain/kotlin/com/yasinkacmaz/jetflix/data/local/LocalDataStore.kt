package com.yasinkacmaz.jetflix.data.local

import kotlinx.coroutines.flow.Flow

interface LocalDataStore {
    fun get(key: String): Flow<String?>

    suspend fun set(key: String, newValue: String)
}
