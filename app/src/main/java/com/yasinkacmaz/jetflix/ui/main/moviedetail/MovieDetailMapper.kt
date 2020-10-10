package com.yasinkacmaz.jetflix.ui.main.moviedetail

import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toBackdropUrl
import com.yasinkacmaz.jetflix.util.toPosterUrl
import javax.inject.Inject

class MovieDetailMapper @Inject constructor() : Mapper<MovieDetailResponse, MovieDetail> {
    override fun map(input: MovieDetailResponse) = MovieDetail(
        id = input.id,
        title = input.title,
        originalTitle = input.originalTitle,
        overview = input.overview,
        backdropUrl = input.backdropPath.orEmpty().toBackdropUrl(),
        posterUrl = input.posterPath.toPosterUrl(),
        genres = input.genres,
        releaseDate = input.releaseDate,
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
        duration = input.runtime ?: -1
    )
}
