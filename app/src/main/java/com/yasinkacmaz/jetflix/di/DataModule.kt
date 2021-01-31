package com.yasinkacmaz.jetflix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.yasinkacmaz.jetflix.ui.main.settings.LanguageDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.createDataStore(name = "settings")
    }

    @Provides
    @Singleton
    fun provideLanguageDataStore(json: Json, preferences: DataStore<Preferences>): LanguageDataStore {
        return LanguageDataStore(json, preferences)
    }
}
