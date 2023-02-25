package com.yasinkacmaz.jetflix.util.service

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.GenresResponse
import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.data.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.MovieResponse
import com.yasinkacmaz.jetflix.data.MoviesResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.util.parseJson

class FakeMovieService : MovieService {
    val genre = Genre(1, "Name")
    var fetchGenresException: Exception? = null
    var movieDetailException: Exception? = null
    val movieDetailResponse: MovieDetailResponse = parseJson("movie_detail.json")
    val creditsResponse: CreditsResponse = parseJson("credits.json")
    val imagesResponse: ImagesResponse = parseJson("images.json")
    val moviesResponse = MoviesResponse(1, listOf(MovieResponse(1, "movie", "", "", "", "", "", 1.1, 1)), 1, 1)
    val searchResponse = MoviesResponse(1, listOf(MovieResponse(1, "search", "", "", "", "", "", 1.1, 1)), 1, 1)

    override suspend fun fetchMovies(pageNumber: Int, options: Map<String, String>): MoviesResponse {
        return moviesResponse
    }

    override suspend fun search(pageNumber: Int, searchQuery: String, includeAdult: Boolean): MoviesResponse {
        return searchResponse
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
