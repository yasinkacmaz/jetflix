package com.yasinkacmaz.jetflix.data.local

import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.w3c.dom.StorageEvent
import org.w3c.dom.events.Event

class WasmJsDataStore : LocalDataStore {
    private val flows = mutableMapOf<String, MutableStateFlow<String?>>()

    init {
        val listener: (Event) -> Unit = listener@{ event ->
            val storageEvent = event as? StorageEvent ?: return@listener
            val key = storageEvent.key ?: return@listener
            val newValue = storageEvent.newValue
            flows[key]?.value = newValue
        }
        window.addEventListener("storage", listener)
    }

    override fun get(key: String): Flow<String?> = flows.getOrPut(key) {
        MutableStateFlow(window.localStorage.getItem(key))
    }

    override suspend fun set(key: String, newValue: String) {
        window.localStorage.setItem(key, newValue)
        flows[key]?.value = newValue
    }
}
