package com.yasinkacmaz.jetflix.data.error

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.io.IOException

class ApiErrorHandler {
    fun handleApiError(error: Throwable): Unit = throw when (error) {
        is ConnectTimeoutException,
        is SocketTimeoutException,
        is HttpRequestTimeoutException,
        -> ApiError.NetworkTimeout

        is UnresolvedAddressException,
        is IOException,
        -> ApiError.NoNetworkConnection

        is CancellationException -> error
        else -> ApiError.Unknown("${error::class.simpleName}: ${error.message.orEmpty()}")
    }
}
