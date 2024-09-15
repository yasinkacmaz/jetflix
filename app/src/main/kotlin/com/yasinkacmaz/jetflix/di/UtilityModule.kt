package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.util.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val utilityModule = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single { Dispatchers() }
}
