package com.yasinkacmaz.jetflix.data.service

import com.yasinkacmaz.jetflix.ui.settings.Language

interface ConfigurationService {
    suspend fun fetchLanguages(): List<Language>
}
