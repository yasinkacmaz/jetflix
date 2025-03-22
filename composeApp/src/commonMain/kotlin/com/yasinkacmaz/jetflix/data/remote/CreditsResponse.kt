package com.yasinkacmaz.jetflix.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditsResponse(
    @SerialName("id") val id: Int,
    @SerialName("cast") val cast: List<CastResponse>,
    @SerialName("crew") val crew: List<CrewResponse>,
)

@Serializable
data class CastResponse(
    @SerialName("cast_id") val castId: Int,
    @SerialName("character") val character: String,
    @SerialName("credit_id") val creditId: String,
    @SerialName("gender") val gender: Int,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("order") val order: Int,
    @SerialName("profile_path") val profilePath: String?,
)

@Serializable
data class CrewResponse(
    @SerialName("credit_id") val creditId: String,
    @SerialName("department") val department: String,
    @SerialName("gender") val gender: Int,
    @SerialName("id") val id: Int,
    @SerialName("job") val job: String,
    @SerialName("name") val name: String,
    @SerialName("profile_path") val profilePath: String?,
)
