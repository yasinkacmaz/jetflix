package com.yasinkacmaz.jetflix.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("also_known_as") val alsoKnownAs: List<String> = emptyList(),
    @SerialName("biography") val biography: String,
    @SerialName("birthday") val birthday: String?,
    @SerialName("id") val id: Int,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("known_for_department") val knownForDepartment: String,
    @SerialName("name") val name: String,
    @SerialName("place_of_birth") val placeOfBirth: String?,
    @SerialName("profile_path") val profilePath: String? = null
)
