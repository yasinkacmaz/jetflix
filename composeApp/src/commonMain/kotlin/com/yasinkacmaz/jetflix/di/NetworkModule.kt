package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest { url("https://api.themoviedb.org/3/") }
            defaultApiKeyParameter("9487082a53af88e1866c341355155846")
        }.apply {
            plugin(HttpSend).intercept { request ->
                val languageDataStore: LanguageDataStore = get()
                val language = languageDataStore.languageCode.first()
                request.url.parameters.append("language", language)
                execute(request)
            }
        }
    }
}

fun HttpClientConfig<*>.defaultApiKeyParameter(apiKey: String) = defaultRequest {
    url {
        parameters.append("api_key", apiKey)
    }
}
