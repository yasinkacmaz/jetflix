package com.yasinkacmaz.jetflix.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope

internal const val PREFERENCES_FILE_NAME = "jetflix_settings.preferences_pb"

expect fun createDataStore(
    filePathProvider: DataStoreFilePathProvider,
    scope: CoroutineScope,
    fileName: String = PREFERENCES_FILE_NAME,
): DataStore<Preferences>
