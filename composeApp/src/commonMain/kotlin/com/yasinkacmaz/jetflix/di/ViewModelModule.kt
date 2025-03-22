package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.ui.favorites.FavoritesViewModel
import com.yasinkacmaz.jetflix.ui.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailViewModel
import com.yasinkacmaz.jetflix.ui.movies.MoviesViewModel
import com.yasinkacmaz.jetflix.ui.profile.ProfileViewModel
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoviesViewModel)
    viewModelOf(::MovieDetailViewModel)
    viewModelOf(::FilterViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoritesViewModel)
}
