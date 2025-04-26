package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.ui.favorites.FavoritesDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::LanguageDataStore)
    singleOf(::FilterDataStore)
    singleOf(::FavoritesDataStore)
}
