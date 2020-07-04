package com.yasinkacmaz.playground.ui.application

import com.yasinkacmaz.playground.di.NetworkModule
import com.yasinkacmaz.playground.di.ServiceModule
import com.yasinkacmaz.playground.di.UtilityModule
import com.yasinkacmaz.playground.ui.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class, AndroidInjectionModule::class, ActivityModule::class,
        ViewModelModule::class, NetworkModule::class, ServiceModule::class, UtilityModule::class
    ]
)
interface ApplicationComponent {

    fun inject(playgroundApplication: PlaygroundApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: PlaygroundApplication): Builder

        fun build(): ApplicationComponent
    }
}
