package com.yasinkacmaz.jetflix.util

import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
}

inline fun <reified T : Any> parseJson(jsonContent: String): T = json.decodeFromString(jsonContent)
