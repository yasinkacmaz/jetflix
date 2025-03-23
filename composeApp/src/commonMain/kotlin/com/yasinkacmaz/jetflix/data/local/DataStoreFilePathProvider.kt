package com.yasinkacmaz.jetflix.data.local

private const val JETFLIX_SETTINGS = "opwire_settings.preferences_pb"

expect class DataStoreFilePathProvider {
    fun provide(preferencesFileName: String = JETFLIX_SETTINGS): String
}
