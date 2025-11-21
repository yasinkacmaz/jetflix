package com.yasinkacmaz.jetflix.ui.movies

import com.yasinkacmaz.jetflix.data.remote.MovieResponse
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.util.toSmallPosterUrl
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MovieMapperTest {
    private val movieResponse = MovieResponse(1, "", "Name", "Title", "Language", "Overview", "Poster", 1.1, 1)

    @Test
    fun map() {
        val movie = MovieMapper().map(movieResponse)

        movie.id shouldBe movieResponse.id
        movie.name shouldBe movieResponse.name
        movie.posterPath shouldBe movieResponse.posterPath.orEmpty().toSmallPosterUrl()
        movie.voteAverage shouldBe movieResponse.voteAverage
        movie.voteCount shouldBe movieResponse.voteCount
    }

    @Test
    fun `Should map vote average with single decimal point`() {
        val movie = MovieMapper().map(movieResponse.copy(voteAverage = 1.234))

        movie.voteAverage shouldBe 1.2
    }

    @Test
    fun `Should format release date correctly`() {
        val movie = MovieMapper().map(movieResponse.copy(firstAirDate = "1994-12-06"))

        movie.releaseDate shouldBe "06.12.1994"
    }
}
