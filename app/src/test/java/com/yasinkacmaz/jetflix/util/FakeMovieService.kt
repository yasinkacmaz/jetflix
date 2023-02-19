package com.yasinkacmaz.jetflix.util

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.GenresResponse
import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService

class FakeMovieService : MovieService {
    val genre = Genre(1, "Name")
    var fetchGenresException: Exception? = null
    var movieDetailException: Exception? = null
    val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")
    val creditsResponse: CreditsResponse = parseJson("credits.json")
    val imagesResponse: ImagesResponse = parseJson("images.json")

    override suspend fun fetchMovies(pageNumber: Int, options: Map<String, String>): MoviesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun search(pageNumber: Int, searchQuery: String, includeAdult: Boolean): MoviesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun fetchGenres(): GenresResponse {
        return if (fetchGenresException == null) {
            GenresResponse(listOf(genre))
        } else {
            throw fetchGenresException!!.also { fetchGenresException = null }
        }
    }

    override suspend fun fetchMovieDetail(movieId: Int): MovieDetailResponse {
        return if (movieDetailException == null) {
            movieDetailResponse
        } else {
            throw movieDetailException!!.also { movieDetailException = null }
        }
    }

    override suspend fun fetchMovieCredits(movieId: Int): CreditsResponse {
        return creditsResponse
    }

    override suspend fun fetchMovieImages(movieId: Int): ImagesResponse {
        return imagesResponse
    }
}
