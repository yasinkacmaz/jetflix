package com.yasinkacmaz.jetflix.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initializeKoin(koinAppDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        koinAppDeclaration?.invoke(this)
        modules(
            appModule,
            platformModule,
            dataModule,
            networkModule,
            servicesModule,
            viewModelModule,
        )
    }
}
