package com.yasinkacmaz.jetflix.ui.profile

import com.yasinkacmaz.jetflix.data.remote.ProfileResponse
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.resource.personJson
import com.yasinkacmaz.jetflix.util.toImdbProfileUrl
import com.yasinkacmaz.jetflix.util.toOriginalUrl
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ProfileMapperTest {
    private val mapper = ProfileMapper()

    private val profileResponse = parseJson<ProfileResponse>(personJson)

    @Test
    fun map() {
        val profile = mapper.map(profileResponse)

        profile.name shouldBe profileResponse.name
        profile.biography shouldBe profileResponse.biography
        profile.birthday shouldBe profileResponse.birthday
        profile.placeOfBirth shouldBe profileResponse.placeOfBirth
        profile.alsoKnownAs shouldBe profileResponse.alsoKnownAs
        profile.imdbProfileUrl shouldBe profileResponse.imdbId?.toImdbProfileUrl()
        profile.profilePhotoUrl shouldBe profileResponse.profilePath?.toOriginalUrl()
        profile.knownFor shouldBe profileResponse.knownForDepartment
    }
}
