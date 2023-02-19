package com.yasinkacmaz.jetflix.util

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.GenresResponse
import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import java.io.IOException

class FakeMovieService : MovieService {
    val genre = Genre(1, "Name")
    var fetchGenresShouldFail = false

    override suspend fun fetchMovies(pageNumber: Int, options: Map<String, String>): MoviesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun search(pageNumber: Int, searchQuery: String, includeAdult: Boolean): MoviesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun fetchGenres(): GenresResponse {
        return if (!fetchGenresShouldFail) GenresResponse(listOf(genre)) else throw IOException()
    }

    override suspend fun fetchMovieDetail(movieId: Int): MovieDetailResponse {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMovieCredits(movieId: Int): CreditsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMovieImages(movieId: Int): ImagesResponse {
        TODO("Not yet implemented")
    }
}