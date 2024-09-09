package com.yasinkacmaz.jetflix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import com.yasinkacmaz.jetflix.data.local.PreferencesDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val dataModule = module {
    single<DataStore<Preferences>> { get<Context>().preferencesDataStore }
    singleOf(::PreferencesDataStore).bind(LocalDataStore::class)
    singleOf(::LanguageDataStore)
    singleOf(::FilterDataStore)
}
