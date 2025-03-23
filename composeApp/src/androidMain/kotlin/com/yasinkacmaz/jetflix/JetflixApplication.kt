package com.yasinkacmaz.jetflix

import android.app.Application
import com.yasinkacmaz.jetflix.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class JetflixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@JetflixApplication)
        }
    }
}
