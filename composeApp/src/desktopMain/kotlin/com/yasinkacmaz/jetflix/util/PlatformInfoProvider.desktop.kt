package com.yasinkacmaz.jetflix.util

actual class PlatformInfoProvider(private val buildType: BuildType) {
    actual val platformInfo: PlatformInfo = PlatformInfo(
        appVersionName = "${System.getProperty("jpackage.app-version") ?: "Unknown"}${buildType.versionSuffix}",
    )
}
