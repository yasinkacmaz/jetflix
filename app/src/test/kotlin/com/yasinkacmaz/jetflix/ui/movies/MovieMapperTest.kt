package com.yasinkacmaz.jetflix.ui.movies

import com.yasinkacmaz.jetflix.data.remote.MovieResponse
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.toPosterUrl
import io.kotest.matchers.shouldBe
import org.junit.Test

class MovieMapperTest {
    private val movieResponse = MovieResponse(1, "Date", "Name", "Title", "Language", "Overview", "Poster", 1.1, 1)

    @Test
    fun map() {
        val movie = MovieMapper().map(movieResponse)

        movie.id shouldBe movieResponse.id
        movie.name shouldBe movieResponse.name
        movie.releaseDate shouldBe movieResponse.firstAirDate
        movie.posterPath shouldBe movieResponse.posterPath.orEmpty().toPosterUrl()
        movie.voteAverage shouldBe movieResponse.voteAverage
        movie.voteCount shouldBe movieResponse.voteCount
    }

    @Test
    fun `Should map vote average with single decimal point`() {
        val movie = MovieMapper().map(movieResponse.copy(voteAverage = 1.234))

        movie.voteAverage shouldBe 1.2
    }
}
