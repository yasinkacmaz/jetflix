package com.yasinkacmaz.jetflix.ui.moviedetail

import com.yasinkacmaz.jetflix.data.remote.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.remote.ProductionCompanyResponse
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.toBackdropUrl
import com.yasinkacmaz.jetflix.util.toPosterUrl
import io.kotest.matchers.shouldBe
import org.junit.Test

class MovieDetailMapperTest {
    private val mapper = MovieDetailMapper()

    private val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")

    @Test
    fun map() {
        val movieDetail = mapper.map(movieDetailResponse)

        movieDetail.id shouldBe movieDetailResponse.id
        movieDetail.title shouldBe movieDetailResponse.title
        movieDetail.originalTitle shouldBe movieDetailResponse.originalTitle
        movieDetail.overview shouldBe movieDetailResponse.overview
        movieDetail.backdropUrl shouldBe movieDetailResponse.backdropPath.orEmpty().toBackdropUrl()
        movieDetail.posterUrl shouldBe movieDetailResponse.posterPath.toPosterUrl()
        movieDetail.genres shouldBe movieDetailResponse.genres
        movieDetail.releaseDate shouldBe movieDetailResponse.releaseDate
        movieDetail.voteAverage shouldBe movieDetailResponse.voteAverage
        movieDetail.voteCount shouldBe movieDetailResponse.voteCount
        movieDetail.duration shouldBe movieDetailResponse.runtime
        movieDetail.homepage shouldBe movieDetailResponse.homepage
    }

    @Test
    fun `Map should remove last dots from tagline`() {
        val movieDetail = mapper.map(movieDetailResponse.copy(tagline = "Tagline.."))

        movieDetail.tagline shouldBe "Tagline"
    }

    @Test
    fun `Map should set production companies with poster url`() {
        val productionCompanyResponse = ProductionCompanyResponse(1, "logoUrl", "Name", "Country")

        val movieDetail = mapper.map(movieDetailResponse.copy(productionCompanies = listOf(productionCompanyResponse)))

        val expectedProductionCompany = ProductionCompany("Name", "logoUrl".toPosterUrl())
        movieDetail.productionCompanies.first() shouldBe expectedProductionCompany
    }
}
