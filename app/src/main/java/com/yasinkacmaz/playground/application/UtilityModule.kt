package com.yasinkacmaz.playground.application

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val utilityModule = module {
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }
}
