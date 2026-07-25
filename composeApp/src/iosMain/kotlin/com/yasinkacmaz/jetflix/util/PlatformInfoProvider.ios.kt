package com.yasinkacmaz.jetflix.util

import platform.Foundation.NSBundle

actual class PlatformInfoProvider(private val buildType: BuildType) {
    actual val platformInfo: PlatformInfo = PlatformInfo(
        appVersionName = appVersionName(),
    )

    private fun appVersionName(): String {
        val bundle =
            (NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String) ?: "Unknown"
        return "$bundle${buildType.versionSuffix}"
    }
}
