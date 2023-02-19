package com.yasinkacmaz.jetflix.util

import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeStringDataStore : LocalDataStore {
    var currentValue: String? = null

    override fun get(key: String): Flow<String?> {
        return flowOf(currentValue)
    }

    override suspend fun set(key: String, newValue: String) {
        currentValue = newValue
    }
}
