package com.yasinkacmaz.jetflix.ui.main.movies

import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toPosterUrl
import javax.inject.Inject

class MovieMapper @Inject constructor() : Mapper<MovieResponse, Movie> {
    override fun map(input: MovieResponse): Movie {
        return Movie(
            id = input.id,
            name = input.name,
            originalName = input.originalTitle,
            overview = input.overview,
            releaseDate = input.firstAirDate,
            posterPath = input.posterPath.orEmpty().toPosterUrl(),
            voteAverage = input.voteAverage,
            voteCount = input.voteCount
        )
    }
}
