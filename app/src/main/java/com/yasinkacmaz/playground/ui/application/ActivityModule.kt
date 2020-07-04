package com.yasinkacmaz.playground.ui.application

import com.yasinkacmaz.playground.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector
    fun contributeMainActivityInjector(): MainActivity
}