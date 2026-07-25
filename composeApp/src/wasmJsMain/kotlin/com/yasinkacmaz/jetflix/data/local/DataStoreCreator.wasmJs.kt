package com.yasinkacmaz.jetflix.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.WebOpfsStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import kotlinx.coroutines.CoroutineScope

actual fun createDataStore(
    filePathProvider: DataStoreFilePathProvider,
    scope: CoroutineScope,
    fileName: String,
): DataStore<Preferences> = DataStoreFactory.create(
    storage = WebOpfsStorage(PreferencesSerializer, fileName),
    scope = scope,
)
