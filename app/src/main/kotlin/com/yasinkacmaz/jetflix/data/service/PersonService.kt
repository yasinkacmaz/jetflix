package com.yasinkacmaz.jetflix.data.service

import com.yasinkacmaz.jetflix.data.remote.ProfileResponse

interface PersonService {
    suspend fun profile(id: Int): ProfileResponse
}
