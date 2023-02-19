package com.yasinkacmaz.jetflix.util.service

import com.yasinkacmaz.jetflix.data.ProfileResponse
import com.yasinkacmaz.jetflix.service.PersonService
import com.yasinkacmaz.jetflix.util.parseJson

class FakePersonService : PersonService {
    var fetchProfileException: Exception? = null
    val profileResponse = parseJson<ProfileResponse>("person.json")

    override suspend fun profile(id: Int): ProfileResponse {
        return if (fetchProfileException == null) {
            profileResponse
        } else {
            throw fetchProfileException!!.also { fetchProfileException = null }
        }
    }
}
