package com.yasinkacmaz.jetflix.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UtilityModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
}
