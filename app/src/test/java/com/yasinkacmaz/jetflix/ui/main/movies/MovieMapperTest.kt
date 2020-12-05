package com.yasinkacmaz.jetflix.ui.main.movies

import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.util.toPosterUrl
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MovieMapperTest {
    @Test
    fun map() {
        val movieResponse = MovieResponse(1, "Date", "Name", "Title", "Language", "Overview", "Poster", 1.1, 1)

        val movie = MovieMapper().map(movieResponse)

        expectThat(movie.id).isEqualTo(movieResponse.id)
        expectThat(movie.name).isEqualTo(movieResponse.name)
        expectThat(movie.releaseDate).isEqualTo(movieResponse.firstAirDate)
        expectThat(movie.posterPath).isEqualTo(movieResponse.posterPath.orEmpty().toPosterUrl())
        expectThat(movie.voteAverage).isEqualTo(movieResponse.voteAverage)
        expectThat(movie.voteCount).isEqualTo(movieResponse.voteCount)
    }
}
