package com.yasinkacmaz.playground.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PlaygroundApplication)
            modules(listOf(netWorkModule, utilityModule))
        }
    }
}
