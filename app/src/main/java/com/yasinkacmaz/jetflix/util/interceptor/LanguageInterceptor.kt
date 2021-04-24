package com.yasinkacmaz.jetflix.util.interceptor

import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class LanguageInterceptor(private val languageDataStore: LanguageDataStore) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val languageCode = runBlocking { languageDataStore.languageCode.first() }
        val url = request.url.newBuilder().addQueryParameter(LANGUAGE, languageCode).build()
        val newRequest = request.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val LANGUAGE = "language"
    }
}
