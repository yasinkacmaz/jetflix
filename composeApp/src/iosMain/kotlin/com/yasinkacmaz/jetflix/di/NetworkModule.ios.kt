package com.yasinkacmaz.jetflix.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun provideHttpClientEngine(): HttpClientEngine = Darwin.create { }
