package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.data.local.DataStoreFilePathProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DataStoreFilePathProvider)
}
