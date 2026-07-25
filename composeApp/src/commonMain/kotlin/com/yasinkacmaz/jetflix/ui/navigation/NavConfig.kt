package com.yasinkacmaz.jetflix.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
val navSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclassesOfSealed<Screen>()
    }
}

val navSavedStateConfig = SavedStateConfiguration {
    serializersModule = navSerializersModule
}

private val navJson = Json { serializersModule = navSerializersModule }

fun encodeScreen(screen: Screen): String = navJson.encodeToString(Screen.serializer(), screen)
fun decodeScreen(json: String): Screen = navJson.decodeFromString(Screen.serializer(), json)
