package com.yasinkacmaz.jetflix.data.local

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual class DataStoreFilePathProvider {
    actual fun provide(preferencesFileName: String): String {
        val fileManager = NSFileManager.defaultManager
        val documentDirectory = fileManager.URLForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask,
            null,
            true,
            null,
        ) ?: throw IllegalStateException("Unable to get document directory")

        val directory = documentDirectory.path + "/DataStore"
        fileManager.createDirectoryAtPath(directory, true, null, null)
        val filePath = "$directory/$preferencesFileName"
        return filePath
    }
}
