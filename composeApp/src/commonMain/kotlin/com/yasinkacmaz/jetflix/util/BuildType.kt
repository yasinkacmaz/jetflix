package com.yasinkacmaz.jetflix.util

enum class BuildType(val versionSuffix: String) {
    DEBUG("-DEBUG"),
    RELEASE(""),
    ;

    companion object {
        fun from(packageName: String?): BuildType = when {
            packageName?.endsWith(".debug") == true -> DEBUG
            else -> RELEASE
        }
    }
}
