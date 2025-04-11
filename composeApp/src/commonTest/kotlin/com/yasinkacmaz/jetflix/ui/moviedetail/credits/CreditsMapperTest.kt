package com.yasinkacmaz.jetflix.ui.moviedetail.credits

import com.yasinkacmaz.jetflix.data.remote.CreditsResponse
import com.yasinkacmaz.jetflix.util.resource.creditsJson
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.toProfilePhotoUrl
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CreditsMapperTest {
    private val mapper = CreditsMapper()

    private val creditsResponse: CreditsResponse = parseJson(creditsJson)

    @Test
    fun `Map gender as male`() {
        val castResponse = creditsResponse.cast.first().copy(gender = 0)

        val credits = mapper.map(creditsResponse.copy(cast = listOf(castResponse)))

        val person = credits.cast.first()
        person.gender shouldBe Gender.MALE
    }

    @Test
    fun `Map gender as female`() {
        val castResponse = creditsResponse.cast.first().copy(gender = 1)

        val credits = mapper.map(creditsResponse.copy(cast = listOf(castResponse)))

        val person = credits.cast.first()
        person.gender shouldBe Gender.FEMALE
    }

    @Test
    fun `Map cast person with character`() {
        val credits = mapper.map(creditsResponse)

        val castPerson = credits.cast.first()
        val castResponse = creditsResponse.cast.first()
        castPerson.name shouldBe castResponse.name
        castPerson.role shouldBe castResponse.character
        castPerson.profilePhotoUrl shouldBe castResponse.profilePath?.toProfilePhotoUrl()
    }

    @Test
    fun `Map crew person with job`() {
        val credits = mapper.map(creditsResponse)

        val crewPerson = credits.crew.first()
        val crewResponse = creditsResponse.crew.first()
        crewPerson.name shouldBe crewResponse.name
        crewPerson.role shouldBe crewResponse.job
        crewPerson.profilePhotoUrl shouldBe crewResponse.profilePath?.toProfilePhotoUrl()
    }
}
