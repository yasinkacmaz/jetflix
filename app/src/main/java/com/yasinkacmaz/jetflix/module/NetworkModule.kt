package com.yasinkacmaz.jetflix.module

import android.content.Context
import com.google.gson.Gson
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.settings.LanguageDataStore
import com.yasinkacmaz.jetflix.util.interceptor.ApiKeyInterceptor
import com.yasinkacmaz.jetflix.util.interceptor.LanguageInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL = "https://api.themoviedb.org/3/"

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideApiKeyInterceptor(@ApplicationContext context: Context): Interceptor {
        return ApiKeyInterceptor(context.getString(R.string.api_key))
    }

    @Provides
    @Singleton
    @IntoSet
    fun provideLanguageInterceptor(languageDataStore: LanguageDataStore): Interceptor {
        return LanguageInterceptor(languageDataStore)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors().addAll(interceptors)
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService = retrofit.create()

    @Provides
    @Singleton
    fun provideConfigurationService(retrofit: Retrofit): ConfigurationService = retrofit.create()
}
