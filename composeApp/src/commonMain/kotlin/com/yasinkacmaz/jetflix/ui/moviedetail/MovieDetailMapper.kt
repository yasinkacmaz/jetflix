package com.yasinkacmaz.jetflix.ui.moviedetail

import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.data.remote.MovieDetailResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.parseAsDate
import com.yasinkacmaz.jetflix.util.toBackdropUrl
import com.yasinkacmaz.jetflix.util.toPosterUrl

class MovieDetailMapper : Mapper<MovieDetailResponse, MovieDetail> {
    override fun map(input: MovieDetailResponse): MovieDetail {
        val productionCompanies = input.productionCompanies.map {
            ProductionCompany(it.name, it.logoPath.orEmpty().toPosterUrl())
        }
        return MovieDetail(
            id = input.id,
            title = input.title,
            originalTitle = input.originalTitle,
            overview = input.overview,
            tagline = input.tagline.dropLastWhile { it == '.' },
            backdropUrl = input.backdropPath.orEmpty().toBackdropUrl(),
            posterUrl = input.posterPath.orEmpty().toPosterUrl(),
            genres = input.genres.mapNotNull(Genre::name).take(4),
            releaseDate = input.releaseDate.parseAsDate(),
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
            duration = input.runtime ?: -1,
            productionCompanies = productionCompanies,
            homepage = input.homepage,
        )
    }
}
