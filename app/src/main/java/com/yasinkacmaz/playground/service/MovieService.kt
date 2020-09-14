package com.yasinkacmaz.playground.service

import com.yasinkacmaz.playground.data.GenresResponse
import com.yasinkacmaz.playground.data.MoviesResponse
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
}
