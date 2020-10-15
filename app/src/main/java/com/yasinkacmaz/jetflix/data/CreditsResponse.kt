package com.yasinkacmaz.jetflix.data
import com.google.gson.annotations.SerializedName


data class CreditsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<CastResponse>,
    @SerializedName("crew") val crew: List<CrewResponse>
)

data class CastResponse(
    @SerializedName("cast_id") val castId: Int,
    @SerializedName("character") val character: String,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("profile_path") val profilePath: String?
)

data class CrewResponse(
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("department") val department: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("job") val job: String,
    @SerializedName("name") val name: String,
    @SerializedName("profile_path") val profilePath: String?
)
