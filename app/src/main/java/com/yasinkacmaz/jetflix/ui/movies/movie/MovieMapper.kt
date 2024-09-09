package com.yasinkacmaz.jetflix.ui.movies.movie

import com.yasinkacmaz.jetflix.data.remote.MovieResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toPosterUrl

class MovieMapper : Mapper<MovieResponse, Movie> {
    override fun map(input: MovieResponse) = Movie(
        id = input.id,
        name = input.name,
        releaseDate = input.firstAirDate.orEmpty(),
        posterPath = input.posterPath.orEmpty().toPosterUrl(),
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
    )
}
