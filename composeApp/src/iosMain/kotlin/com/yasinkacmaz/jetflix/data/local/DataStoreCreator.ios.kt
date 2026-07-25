package com.yasinkacmaz.jetflix.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath

actual fun createDataStore(
    filePathProvider: DataStoreFilePathProvider,
    scope: CoroutineScope,
    fileName: String,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(scope = scope) {
    filePathProvider.provide().toPath()
}
