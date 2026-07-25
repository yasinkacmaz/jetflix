package com.yasinkacmaz.jetflix.util

import android.content.Context

actual class PlatformInfoProvider(private val context: Context, private val buildType: BuildType) {
    actual val platformInfo: PlatformInfo = PlatformInfo(
        appVersionName = appVersionName(context),
    )

    private fun appVersionName(context: Context): String {
        val versionName = runCatching {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: "Unknown"
        }.getOrDefault("Unknown")
        return if (versionName.endsWith(buildType.versionSuffix)) {
            versionName
        } else {
            "$versionName${buildType.versionSuffix}"
        }
    }
}
