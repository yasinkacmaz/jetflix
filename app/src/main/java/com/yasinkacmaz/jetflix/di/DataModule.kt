package com.yasinkacmaz.jetflix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideLanguageDataStore(@ApplicationContext context: Context, json: Json): LanguageDataStore {
        return LanguageDataStore(json, context.preferencesDataStore)
    }

    @Provides
    @Singleton
    fun provideFilterDataStore(@ApplicationContext context: Context, json: Json): FilterDataStore {
        return FilterDataStore(json, context.preferencesDataStore)
    }
}
