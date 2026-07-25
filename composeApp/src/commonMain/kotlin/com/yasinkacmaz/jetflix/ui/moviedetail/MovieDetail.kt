package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.compose.runtime.Immutable

@Immutable
data class MovieDetail(
    val id: Int,
    val title: String = "",
    val originalTitle: String = "",
    val tagline: String = "",
    val overview: String = "",
    val backdropUrl: String = "",
    val posterUrl: String = "",
    val genres: List<String> = emptyList(),
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val duration: Int = 0,
    val productionCompanies: List<ProductionCompany> = emptyList(),
    val homepage: String? = null,
)

@Immutable
data class ProductionCompany(val name: String, val logoUrl: String)
