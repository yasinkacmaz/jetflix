package com.yasinkacmaz.jetflix.data.error

sealed class ApiError(override val message: String?) : Exception(message) {
    data object NetworkTimeout : ApiError("Network Timeout")
    data object NoNetworkConnection : ApiError("No Network Connection")
    data class Unknown(val errorMessage: String?) : ApiError(errorMessage)
}
