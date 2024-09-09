package com.yasinkacmaz.jetflix.di

import android.content.Context
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.util.interceptor.ApiKeyInterceptor
import com.yasinkacmaz.jetflix.util.interceptor.LanguageInterceptor
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import okhttp3.Interceptor
import org.koin.dsl.module

val networkModule = module {
    single<Set<Interceptor>> {
        setOf(
            ApiKeyInterceptor(get<Context>().getString(R.string.api_key)),
            LanguageInterceptor(get()),
        )
    }

    single<HttpClientEngine> {
        OkHttp.create {
            config {
                interceptors().addAll(get<Set<Interceptor>>())
            }
        }
    }

    single<HttpClient> {
        HttpClient(get<HttpClientEngine>()) {
            install(ContentNegotiation) {
                json(get())
            }
            defaultRequest {
                url("https://api.themoviedb.org/3/")
            }
        }
    }
}
