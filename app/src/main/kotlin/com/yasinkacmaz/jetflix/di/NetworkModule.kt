package com.yasinkacmaz.jetflix.di

import android.content.Context
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

val networkModule = module {
    single<HttpClientEngine> { OkHttp.create() }

    single<HttpClient> {
        HttpClient(get<HttpClientEngine>()) {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest { url("https://api.themoviedb.org/3/") }
            defaultApiKeyParameter(get<Context>().getString(R.string.api_key))
            defaultLanguageParameter(get())
        }
    }
}

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
