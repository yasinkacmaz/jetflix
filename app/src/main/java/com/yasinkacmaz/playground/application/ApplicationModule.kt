package com.yasinkacmaz.playground.application

import android.content.Context
import com.yasinkacmaz.playground.di.AppContext
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ApplicationModule {
    @Binds
    @Singleton
    @AppContext
    fun bindApplicationContext(application: PlaygroundApplication): Context
}
