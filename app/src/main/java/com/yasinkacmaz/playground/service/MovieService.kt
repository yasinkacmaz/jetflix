package com.yasinkacmaz.playground.service

import com.yasinkacmaz.playground.data.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("tv/popular")
    suspend fun fetchPopularMovies(@Query("page") pageNumber: Int): MoviesResponse
}
