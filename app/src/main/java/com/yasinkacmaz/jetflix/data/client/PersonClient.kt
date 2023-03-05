package com.yasinkacmaz.jetflix.data.client

import com.yasinkacmaz.jetflix.data.remote.ProfileResponse
import com.yasinkacmaz.jetflix.data.service.PersonService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PersonClient(private val httpClient: HttpClient) : PersonService {
    override suspend fun profile(id: Int): ProfileResponse = httpClient.get("person/$id").body()
}
