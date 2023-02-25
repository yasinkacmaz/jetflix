package com.yasinkacmaz.jetflix.ui.filter

import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FilterDataStore(private val json: Json, private val localDataStore: LocalDataStore) {
    val filterState: Flow<FilterState> = localDataStore.get(KEY_FILTER_STATE)
        .map { filterStateString ->
            if (filterStateString != null) {
                json.decodeFromString(filterStateString)
            } else {
                FilterState()
            }
        }
        .catch {
            emit(FilterState())
        }

    suspend fun onFilterStateChanged(filterState: FilterState) {
        localDataStore.set(KEY_FILTER_STATE, json.encodeToString(filterState))
    }

    suspend fun resetFilterState() {
        localDataStore.set(KEY_FILTER_STATE, json.encodeToString(FilterState()))
    }

    companion object {
        private const val KEY_FILTER_STATE = "filter_state"
    }
}
