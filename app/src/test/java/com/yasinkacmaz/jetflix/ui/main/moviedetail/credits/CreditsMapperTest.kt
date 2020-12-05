package com.yasinkacmaz.jetflix.ui.main.moviedetail.credits

import com.yasinkacmaz.jetflix.data.CreditsResponse
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.toProfilePhotoUrl
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CreditsMapperTest {
    private val mapper = CreditsMapper()

    private val creditsResponse: CreditsResponse = parseJson("credits.json")

    @Test
    fun `Map gender as male`() {
        val castResponse = creditsResponse.cast.first().copy(gender = 0)

        val credits = mapper.map(creditsResponse.copy(cast = listOf(castResponse)))

        val person = credits.cast.first()
        expectThat(person.gender).isEqualTo(Gender.MALE)
    }

    @Test
    fun `Map gender as female`() {
        val castResponse = creditsResponse.cast.first().copy(gender = 1)

        val credits = mapper.map(creditsResponse.copy(cast = listOf(castResponse)))

        val person = credits.cast.first()
        expectThat(person.gender).isEqualTo(Gender.FEMALE)
    }

    @Test
    fun `Map cast person with character`() {
        val credits = mapper.map(creditsResponse)

        val castPerson = credits.cast.first()
        val castResponse = creditsResponse.cast.first()
        expectThat(castPerson.name).isEqualTo(castResponse.name)
        expectThat(castPerson.character).isEqualTo(castResponse.character)
        expectThat(castPerson.profilePhotoUrl).isEqualTo(castResponse.profilePath?.toProfilePhotoUrl())
    }

    @Test
    fun `Map crew person with job`() {
        val credits = mapper.map(creditsResponse)

        val crewPerson = credits.crew.first()
        val crewResponse = creditsResponse.crew.first()
        expectThat(crewPerson.name).isEqualTo(crewResponse.name)
        expectThat(crewPerson.character).isEqualTo(crewResponse.job)
        expectThat(crewPerson.profilePhotoUrl).isEqualTo(crewResponse.profilePath?.toProfilePhotoUrl())
    }
}
