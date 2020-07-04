package com.yasinkacmaz.playground.di

import com.yasinkacmaz.playground.api.MovieService
import dagger.Module
import retrofit2.Retrofit
import retrofit2.create

@Module
interface ServiceModule {
    fun provideMovieService(retrofit: Retrofit): MovieService = retrofit.create()
}
