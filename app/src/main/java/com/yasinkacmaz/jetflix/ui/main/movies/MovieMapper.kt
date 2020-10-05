package com.yasinkacmaz.jetflix.ui.main.movies

import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.util.toPosterUrl
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    fun map(movieResponses: List<MovieResponse>): List<Movie> {
        return movieResponses.map { movieResponse ->
            Movie(
                id = movieResponse.id,
                name = movieResponse.name,
                originalName = movieResponse.originalTitle,
                overview = movieResponse.overview,
                releaseDate = movieResponse.firstAirDate,
                posterPath = movieResponse.posterPath.orEmpty().toPosterUrl(),
                voteAverage = movieResponse.voteAverage,
                voteCount = movieResponse.voteCount
            )
        }
    }
}
