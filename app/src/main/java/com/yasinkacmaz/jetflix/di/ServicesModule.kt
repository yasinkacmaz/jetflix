package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.data.client.ConfigurationClient
import com.yasinkacmaz.jetflix.data.client.MovieClient
import com.yasinkacmaz.jetflix.data.client.PersonClient
import com.yasinkacmaz.jetflix.data.service.ConfigurationService
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.data.service.PersonService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val servicesModule = module {
    singleOf(::MovieClient).bind(MovieService::class)
    singleOf(::ConfigurationClient).bind(ConfigurationService::class)
    singleOf(::PersonClient).bind(PersonService::class)
}
