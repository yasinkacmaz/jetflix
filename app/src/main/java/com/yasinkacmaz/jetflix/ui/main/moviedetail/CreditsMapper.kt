package com.yasinkacmaz.jetflix.ui.main.moviedetail

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toProfilePhotoUrl
import javax.inject.Inject

class CreditsMapper @Inject constructor() : Mapper<CreditsResponse, Credits> {
    override fun map(input: CreditsResponse): Credits {
        val cast =
            input.cast.map { Cast(it.name, it.character, it.profilePath?.toProfilePhotoUrl(), it.gender.toGender()) }
        val crew = input.crew.map { Crew(it.name, it.job, it.profilePath?.toProfilePhotoUrl(), it.gender.toGender()) }
        return Credits(cast, crew)
    }

    private fun Int.toGender() = if (this == 1) Gender.FEMALE else Gender.MALE
}
