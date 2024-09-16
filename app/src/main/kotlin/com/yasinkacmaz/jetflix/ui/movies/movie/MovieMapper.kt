package com.yasinkacmaz.jetflix.ui.movies.movie

import com.yasinkacmaz.jetflix.data.remote.MovieResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toPosterUrl
import kotlinx.datetime.LocalDate

class MovieMapper : Mapper<MovieResponse, Movie> {
    override fun map(input: MovieResponse) = Movie(
        id = input.id,
        name = input.name,
        releaseDate = parseReleaseDate(input.firstAirDate),
        posterPath = input.posterPath.orEmpty().toPosterUrl(),
        voteAverage = (input.voteAverage * 10).toInt() / 10.0,
        voteCount = input.voteCount,
    )

    private fun parseReleaseDate(releaseDate: String?): String {
        if (releaseDate == null) return ""

        val parsed = try {
            LocalDate.parse(releaseDate)
        } catch (exception: Exception) {
            return ""
        }

        fun Int.asTwoDigits(): String = this.toString().padStart(2, '0')
        return "${parsed.dayOfMonth.asTwoDigits()}.${parsed.monthNumber.asTwoDigits()}.${parsed.year.asTwoDigits()}"
    }
}
