package com.yasinkacmaz.jetflix.data.client

import com.yasinkacmaz.jetflix.data.service.ConfigurationService
import com.yasinkacmaz.jetflix.ui.settings.Language
import com.yasinkacmaz.jetflix.util.parseBody
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ConfigurationClient(private val httpClient: HttpClient) : ConfigurationService {
    override suspend fun fetchLanguages(): List<Language> = httpClient.get("configuration/languages").parseBody()
}
