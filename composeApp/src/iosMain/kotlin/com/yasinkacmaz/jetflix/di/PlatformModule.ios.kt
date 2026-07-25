package com.yasinkacmaz.jetflix.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.data.local.DataStoreFilePathProvider
import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import com.yasinkacmaz.jetflix.data.local.PreferencesLocalDataStore
import com.yasinkacmaz.jetflix.data.local.createDataStore
import com.yasinkacmaz.jetflix.util.BuildType
import com.yasinkacmaz.jetflix.util.PlatformInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import platform.Foundation.NSBundle

actual val platformModule = module {
    singleOf(::DataStoreFilePathProvider)
    single { BuildType.from(NSBundle.mainBundle.bundleIdentifier) }
    singleOf(::PlatformInfoProvider)
    single { get<PlatformInfoProvider>().platformInfo }
    single<DataStore<Preferences>> {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        createDataStore(get(), scope)
    }
    single<LocalDataStore> { PreferencesLocalDataStore(get()) }
}
