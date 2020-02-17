package com.yasinkacmaz.playground.ui.application

import android.content.Context
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
