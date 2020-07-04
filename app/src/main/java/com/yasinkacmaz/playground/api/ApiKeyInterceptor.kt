package com.yasinkacmaz.playground.api

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter(PARAM_API_KEY, apiKey).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object {
        private const val PARAM_API_KEY = "api_key"
    }
}