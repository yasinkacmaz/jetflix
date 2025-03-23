package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::provideHttpClientEngine)

    single<HttpClient> {
        HttpClient(get<HttpClientEngine>()) {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest { url("https://api.themoviedb.org/3/") }
            defaultApiKeyParameter("9487082a53af88e1866c341355155846")
            defaultLanguageParameter(get())
        }
    }
}

expect fun provideHttpClientEngine(): HttpClientEngine

fun HttpClientConfig<*>.defaultApiKeyParameter(apiKey: String) = defaultRequest {
    url {
        parameters.append("api_key", apiKey)
    }
}

fun HttpClientConfig<*>.defaultLanguageParameter(languageDataStore: LanguageDataStore) = defaultRequest {
    url {
        parameters.append("language", runBlocking { languageDataStore.languageCode.first() })
    }
}
