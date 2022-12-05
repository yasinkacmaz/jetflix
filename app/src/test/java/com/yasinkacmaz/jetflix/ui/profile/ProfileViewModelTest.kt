package com.yasinkacmaz.jetflix.ui.profile

import androidx.lifecycle.SavedStateHandle
import com.yasinkacmaz.jetflix.data.ProfileResponse
import com.yasinkacmaz.jetflix.service.PersonService
import com.yasinkacmaz.jetflix.ui.navigation.ARG_PERSON_ID
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.io.IOException

@ExperimentalCoroutinesApi
class ProfileViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var personService: PersonService

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    private val profileMapper = ProfileMapper()

    private val personId = 1337

    private lateinit var profileViewModel: ProfileViewModel

    private val profileResponse = parseJson<ProfileResponse>("person.json")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { savedStateHandle.get<String>(ARG_PERSON_ID) } returns personId.toString()
        coEvery { personService.profile(personId) } returns profileResponse
    }

    @Test
    fun `fetch profile success`() = runTest {
        profileViewModel = createViewModel()

        val stateValues = profileViewModel.uiState.test()

        coVerify { personService.profile(personId) }
        expectThat(stateValues.last()).isEqualTo(
            ProfileViewModel.ProfileUiState(profileMapper.map(profileResponse), loading = false)
        )
    }

    @Test
    fun `fetch profile error`() = runTest {
        coEvery { personService.profile(personId) } throws IOException()
        profileViewModel = createViewModel()

        val stateValues = profileViewModel.uiState.test()

        coVerify { personService.profile(personId) }
        expectThat(stateValues.last().error).isA<IOException>()
        expectThat(stateValues.last().profile).isNull()
    }

    private fun createViewModel() = ProfileViewModel(savedStateHandle, personService, profileMapper)
}
