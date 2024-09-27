package com.yasinkacmaz.jetflix.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import com.yasinkacmaz.jetflix.data.local.PreferencesDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val PREFERENCES_FILE_NAME = "jetflix_settings.preferences_pb"

val dataModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            get<Context>().filesDir.path.plus("/DataStore/").plus(PREFERENCES_FILE_NAME).toPath()
        }
    }
    singleOf(::PreferencesDataStore).bind(LocalDataStore::class)
    singleOf(::LanguageDataStore)
    singleOf(::FilterDataStore)
}
