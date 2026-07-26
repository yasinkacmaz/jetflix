package com.yasinkacmaz.jetflix.util

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T> HttpResponse.parseBody(): T = withContext(Dispatchers.Default) { body() }
