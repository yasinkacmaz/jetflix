package com.yasinkacmaz.jetflix.util

actual class PlatformInfoProvider(private val buildType: BuildType) {
    actual val platformInfo: PlatformInfo = PlatformInfo(
        appVersionName = "${WasmVersionInfo.VERSION_NAME}${buildType.versionSuffix}",
    )
}
