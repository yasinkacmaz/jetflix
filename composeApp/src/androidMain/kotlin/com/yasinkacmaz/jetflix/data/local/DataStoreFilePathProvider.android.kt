package com.yasinkacmaz.jetflix.data.local

import android.content.Context

actual class DataStoreFilePathProvider(private val context: Context) {
    actual fun provide(preferencesFileName: String): String {
        return context.filesDir.path.plus("/DataStore/").plus(preferencesFileName)
    }
}
