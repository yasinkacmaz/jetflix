package com.yasinkacmaz.jetflix.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    @SerialName("backdrops") val backdrops: List<ImageResponse>,
    @SerialName("id") val id: Int,
    @SerialName("posters") val posters: List<ImageResponse>,
)

@Serializable
data class ImageResponse(
    @SerialName("aspect_ratio") val aspectRatio: Double,
    @SerialName("file_path") val filePath: String,
    @SerialName("height") val height: Int,
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("width") val width: Int,
)
