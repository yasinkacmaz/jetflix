package com.yasinkacmaz.jetflix.service

import com.yasinkacmaz.jetflix.data.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun fetchMovies(
        @Query("with_genres") genreId: Int,
        @Query("page") pageNumber: Int,
        @Query("sort_by") sortBy: String = "vote_count.desc"
    ): MoviesResponse

    @GET("genre/movie/list")
    suspend fun fetchGenres(): GenresResponse

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetail(@Path("movie_id") movieId: Int): MovieDetailResponse

    @GET("movie/{movie_id}/credits")
    suspend fun fetchMovieCredits(@Path("movie_id") movieId: Int): CreditsResponse

    @GET("movie/{movie_id}/images")
    suspend fun fetchMovieImages(@Path("movie_id") movieId: Int): ImagesResponse
}
