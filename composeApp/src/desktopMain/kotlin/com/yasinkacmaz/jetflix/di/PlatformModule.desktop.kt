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
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DataStoreFilePathProvider)
    single { determineBuildType() }
    singleOf(::PlatformInfoProvider)
    single { get<PlatformInfoProvider>().platformInfo }
    single<DataStore<Preferences>> {
        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        createDataStore(get(), scope)
    }
    single<LocalDataStore> { PreferencesLocalDataStore(get()) }
}

private fun determineBuildType(): BuildType {
    val isPackaged = System.getProperty("jpackage.app-version") != null
    return if (isPackaged) BuildType.RELEASE else BuildType.DEBUG
}
