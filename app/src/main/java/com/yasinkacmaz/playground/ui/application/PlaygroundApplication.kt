package com.yasinkacmaz.playground.ui.application

import android.app.Application
import com.yasinkacmaz.playground.util.unsafeLazy
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PlaygroundApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val applicationComponent: ApplicationComponent by unsafeLazy(::buildComponent)

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private fun buildComponent() = DaggerApplicationComponent.builder()
        .application(this)
        .build()
}
