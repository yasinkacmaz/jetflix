package com.yasinkacmaz.jetflix.data.local

actual class DataStoreFilePathProvider {
    actual fun provide(preferencesFileName: String): String =
        System.getProperty("user.home").plus("/.jetflix/DataStore/").plus(preferencesFileName)
}
