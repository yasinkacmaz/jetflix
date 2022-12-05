package com.yasinkacmaz.jetflix.service

import com.yasinkacmaz.jetflix.data.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonService {
    @GET("person/{id}")
    suspend fun profile(@Path("id") id: Int): ProfileResponse
}
