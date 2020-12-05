package com.yasinkacmaz.jetflix.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import com.google.gson.Gson
import com.yasinkacmaz.jetflix.ui.main.settings.LanguageDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideSettingsModel(gson: Gson, preferences: DataStore<Preferences>): LanguageDataStore {
        return LanguageDataStore(gson, preferences)
    }
}
