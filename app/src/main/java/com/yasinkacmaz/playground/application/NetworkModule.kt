package com.yasinkacmaz.playground.application

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val netWorkModule = module {
        single {
            OkHttpClient
                .Builder()
                .build()
        }

        single {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(get()))
                .baseUrl("")
                .build()
        }
}
