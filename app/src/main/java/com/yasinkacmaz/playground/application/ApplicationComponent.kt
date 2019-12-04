package com.yasinkacmaz.playground.application

import com.yasinkacmaz.playground.viewmodel.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class, AndroidSupportInjectionModule::class, ActivityModule::class,
        ViewModelModule::class, NetworkModule::class, UtilityModule::class
    ]
)
interface ApplicationComponent {
}
