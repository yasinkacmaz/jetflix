package com.yasinkacmaz.jetflix.ui.profile

import com.yasinkacmaz.jetflix.data.ProfileResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toImdbProfileUrl
import com.yasinkacmaz.jetflix.util.toOriginalUrl
import javax.inject.Inject

class ProfileMapper @Inject constructor() : Mapper<ProfileResponse, Profile> {
    override fun map(input: ProfileResponse) = Profile(
        name = input.name,
        biography = input.biography,
        birthday = input.birthday.orEmpty(),
        placeOfBirth = input.placeOfBirth.orEmpty(),
        alsoKnownAs = input.alsoKnownAs,
        imdbProfileUrl = input.imdbId?.toImdbProfileUrl(),
        profilePhotoUrl = input.profilePath?.toOriginalUrl().orEmpty(),
        knownFor = input.knownForDepartment
    )
}
