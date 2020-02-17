package com.yasinkacmaz.playground.util

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder().addQueryParameter(API_KEY, KEY).build()
        val newRequest = request.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val API_KEY = "api_key"
        private const val KEY = "35ef0461fc4557cf1d256d3335ed7545"
    }
}
