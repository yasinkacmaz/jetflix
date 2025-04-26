package com.yasinkacmaz.jetflix.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.data.local.AndroidDataStore
import com.yasinkacmaz.jetflix.data.local.DataStoreFilePathProvider
import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DataStoreFilePathProvider)
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath { get<DataStoreFilePathProvider>().provide().toPath() }
    }
    single<LocalDataStore> { AndroidDataStore(get()) }
}
