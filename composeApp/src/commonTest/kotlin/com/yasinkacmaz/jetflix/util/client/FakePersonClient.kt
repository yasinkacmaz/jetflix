package com.yasinkacmaz.jetflix.util.client

import com.yasinkacmaz.jetflix.data.remote.ProfileResponse
import com.yasinkacmaz.jetflix.data.service.PersonService
import com.yasinkacmaz.jetflix.util.resource.personJson
import com.yasinkacmaz.jetflix.util.parseJson

class FakePersonClient : PersonService {
    var fetchProfileException: Exception? = null
    val profileResponse = parseJson<ProfileResponse>(personJson)

    override suspend fun profile(id: Int): ProfileResponse {
        return if (fetchProfileException == null) {
            profileResponse
        } else {
            throw fetchProfileException!!.also { fetchProfileException = null }
        }
    }
}
