package com.yasinkacmaz.jetflix.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.data.local.DataStoreFilePathProvider
import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import com.yasinkacmaz.jetflix.data.local.PreferencesDataStore
import com.yasinkacmaz.jetflix.ui.favorites.FavoritesDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath { get<DataStoreFilePathProvider>().provide().toPath() }
    }
    singleOf(::PreferencesDataStore).bind(LocalDataStore::class)
    singleOf(::LanguageDataStore)
    singleOf(::FilterDataStore)
    singleOf(::FavoritesDataStore)
}
