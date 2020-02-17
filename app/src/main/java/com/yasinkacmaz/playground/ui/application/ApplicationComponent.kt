package com.yasinkacmaz.playground.ui.application

import com.yasinkacmaz.playground.di.NetworkModule
import com.yasinkacmaz.playground.di.UtilityModule
import com.yasinkacmaz.playground.ui.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class, UtilityModule::class, ViewModelModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: PlaygroundApplication)

        fun build(): ApplicationComponent
    }
}
