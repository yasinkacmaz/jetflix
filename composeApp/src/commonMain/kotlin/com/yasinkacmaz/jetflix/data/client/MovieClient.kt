package com.yasinkacmaz.jetflix.data.client

import com.yasinkacmaz.jetflix.data.remote.CreditsResponse
import com.yasinkacmaz.jetflix.data.remote.GenresResponse
import com.yasinkacmaz.jetflix.data.remote.ImagesResponse
import com.yasinkacmaz.jetflix.data.remote.MovieDetailResponse
import com.yasinkacmaz.jetflix.data.remote.MoviesResponse
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.util.parseBody
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MovieClient(private val httpClient: HttpClient) : MovieService {

    override suspend fun fetchMovies(pageNumber: Int, options: Map<String, String>): MoviesResponse =
        httpClient.get("discover/movie") {
            url {
                parameters.append("page", pageNumber.toString())
                options.forEach {
                    parameter(it.key, it.value)
                }
            }
        }.parseBody()

    override suspend fun search(pageNumber: Int, searchQuery: String, includeAdult: Boolean): MoviesResponse =
        httpClient.get("search/movie") {
            url {
                parameters.append("page", pageNumber.toString())
                parameters.append("query", searchQuery)
                parameters.append("include_adult", includeAdult.toString())
            }
        }.parseBody()

    override suspend fun fetchGenres(): GenresResponse = httpClient.get("genre/movie/list").parseBody()

    override suspend fun fetchMovieDetail(movieId: Int): MovieDetailResponse =
        httpClient.get("movie/$movieId").parseBody()

    override suspend fun fetchMovieCredits(movieId: Int): CreditsResponse =
        httpClient.get("movie/$movieId/credits").parseBody()

    override suspend fun fetchMovieImages(movieId: Int): ImagesResponse =
        httpClient.get("movie/$movieId/images").parseBody()
}
