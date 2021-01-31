package com.yasinkacmaz.jetflix.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String
)
