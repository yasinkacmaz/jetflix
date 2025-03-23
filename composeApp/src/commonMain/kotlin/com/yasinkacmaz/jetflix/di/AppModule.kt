package com.yasinkacmaz.jetflix.di

import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.ui.profile.ProfileMapper
import com.yasinkacmaz.jetflix.util.AppDispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { AppDispatchers() }
    singleOf(::MovieRequestOptionsMapper)
    singleOf(::GenreUiModelMapper)
    singleOf(::MovieDetailMapper)
    singleOf(::CreditsMapper)
    singleOf(::ImageMapper)
    singleOf(::MovieMapper)
    singleOf(::ProfileMapper)
}
