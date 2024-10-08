package com.yasinkacmaz.jetflix.ui.application

import android.app.Application
import com.yasinkacmaz.jetflix.di.appModule
import com.yasinkacmaz.jetflix.di.dataModule
import com.yasinkacmaz.jetflix.di.networkModule
import com.yasinkacmaz.jetflix.di.servicesModule
import com.yasinkacmaz.jetflix.di.utilityModule
import com.yasinkacmaz.jetflix.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

class JetflixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@JetflixApplication)
        }
    }

    private fun initializeKoin(koinAppDeclaration: KoinAppDeclaration? = null) {
        startKoin {
            koinAppDeclaration?.invoke(this)
            modules(
                appModule,
                dataModule,
                networkModule,
                servicesModule,
                utilityModule,
                viewModelModule,
            )
        }
    }
}
