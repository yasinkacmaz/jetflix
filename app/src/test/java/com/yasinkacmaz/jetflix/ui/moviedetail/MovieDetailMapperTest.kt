package com.yasinkacmaz.jetflix.ui.moviedetail

import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.ProductionCompanyResponse
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.toBackdropUrl
import com.yasinkacmaz.jetflix.util.toPosterUrl
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MovieDetailMapperTest {
    private val mapper = MovieDetailMapper()

    private val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")

    @Test
    fun map() {
        val movieDetail = mapper.map(movieDetailResponse)

        expectThat(movieDetail.id).isEqualTo(movieDetailResponse.id)
        expectThat(movieDetail.title).isEqualTo(movieDetailResponse.title)
        expectThat(movieDetail.originalTitle).isEqualTo(movieDetailResponse.originalTitle)
        expectThat(movieDetail.overview).isEqualTo(movieDetailResponse.overview)
        expectThat(movieDetail.backdropUrl).isEqualTo(movieDetailResponse.backdropPath.orEmpty().toBackdropUrl())
        expectThat(movieDetail.posterUrl).isEqualTo(movieDetailResponse.posterPath.toPosterUrl())
        expectThat(movieDetail.genres).isEqualTo(movieDetailResponse.genres)
        expectThat(movieDetail.releaseDate).isEqualTo(movieDetailResponse.releaseDate)
        expectThat(movieDetail.voteAverage).isEqualTo(movieDetailResponse.voteAverage)
        expectThat(movieDetail.voteCount).isEqualTo(movieDetailResponse.voteCount)
        expectThat(movieDetail.duration).isEqualTo(movieDetailResponse.runtime)
        expectThat(movieDetail.homepage).isEqualTo(movieDetailResponse.homepage)
    }

    @Test
    fun `Map should remove last dots from tagline`() {
        val movieDetail = mapper.map(movieDetailResponse.copy(tagline = "Tagline.."))

        expectThat(movieDetail.tagline).isEqualTo("Tagline")
    }

    @Test
    fun `Map should set production companies with poster url`() {
        val productionCompanyResponse = ProductionCompanyResponse(1, "logoUrl", "Name", "Country")

        val movieDetail = mapper.map(movieDetailResponse.copy(productionCompanies = listOf(productionCompanyResponse)))

        val expectedProductionCompany = ProductionCompany("Name", "logoUrl".toPosterUrl())
        expectThat(movieDetail.productionCompanies.first()).isEqualTo(expectedProductionCompany)
    }
}
