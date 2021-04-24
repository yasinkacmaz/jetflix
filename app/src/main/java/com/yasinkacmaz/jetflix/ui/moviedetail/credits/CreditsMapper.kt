package com.yasinkacmaz.jetflix.ui.moviedetail.credits

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toProfilePhotoUrl
import javax.inject.Inject

class CreditsMapper @Inject constructor() : Mapper<CreditsResponse, Credits> {
    override fun map(input: CreditsResponse): Credits {
        val cast = input.cast.map { cast ->
            Person(cast.name, cast.character, cast.profilePath?.toProfilePhotoUrl(), cast.gender.toGender())
        }
        val crew = input.crew.map { crew ->
            Person(crew.name, crew.job, crew.profilePath?.toProfilePhotoUrl(), crew.gender.toGender())
        }
        return Credits(cast, crew)
    }

    private fun Int.toGender() = if (this == 1) Gender.FEMALE else Gender.MALE
}
