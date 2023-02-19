package com.yasinkacmaz.jetflix.util.service

import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.ui.settings.Language

class FakeConfigurationService : ConfigurationService {
    var fetchLanguagesException: Exception? = null
    var languages = listOf<Language>()

    override suspend fun fetchLanguages(): List<Language> {
        return if (fetchLanguagesException == null) {
            languages
        } else {
            throw fetchLanguagesException!!.also { fetchLanguagesException = null }
        }
    }
}
