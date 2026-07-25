package com.yasinkacmaz.jetflix.util

data class PlatformInfo(val appVersionName: String)

expect class PlatformInfoProvider {
    val platformInfo: PlatformInfo
}
