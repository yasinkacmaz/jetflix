package com.yasinkacmaz.jetflix.service

import com.yasinkacmaz.jetflix.ui.main.settings.Language
import retrofit2.http.GET

interface ConfigurationService {
    @GET("configuration/languages")
    suspend fun fetchLanguages(): List<Language>
}
