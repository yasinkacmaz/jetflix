package com.yasinkacmaz.playground.application

import com.yasinkacmaz.playground.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(mainActivity: MainActivity)
}
