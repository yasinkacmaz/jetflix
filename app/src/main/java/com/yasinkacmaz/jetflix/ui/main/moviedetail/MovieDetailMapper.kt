package com.yasinkacmaz.jetflix.ui.main.moviedetail

import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.util.toBackdropUrl
import com.yasinkacmaz.jetflix.util.toPosterUrl
import javax.inject.Inject

class MovieDetailMapper @Inject constructor() {
    fun map(response: MovieDetailResponse) = MovieDetail(
        id = response.id,
        title = response.title,
        originalTitle = response.originalTitle,
        overview = response.overview,
        backdropUrl = response.backdropPath.orEmpty().toBackdropUrl(),
        posterUrl = response.posterPath.toPosterUrl(),
        genres = response.genres,
        releaseDate = response.releaseDate,
        voteAverage = response.voteAverage,
        voteCount = response.voteCount,
        duration = response.runtime ?: -1
    )
}
