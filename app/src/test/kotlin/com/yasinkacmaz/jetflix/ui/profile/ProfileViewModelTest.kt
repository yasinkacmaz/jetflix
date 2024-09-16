package com.yasinkacmaz.jetflix.ui.profile

import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.client.FakePersonClient
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val personId = 1337
    private val personService = FakePersonClient()
    private val profileMapper = ProfileMapper()

    private lateinit var profileViewModel: ProfileViewModel

    @Test
    fun `fetch profile success`() = runTest {
        profileViewModel = createViewModel()

        val stateValues = profileViewModel.uiState.test()

        stateValues.last() shouldBe ProfileViewModel.ProfileUiState(
            profileMapper.map(personService.profileResponse),
            loading = false,
        )
    }

    @Test
    fun `fetch profile error`() = runTest {
        personService.fetchProfileException = IOException()

        profileViewModel = createViewModel()
        val stateValues = profileViewModel.uiState.test()

        stateValues.last().error.shouldBeInstanceOf<IOException>()
        stateValues.last().profile shouldBe null
    }

    private fun createViewModel() = ProfileViewModel(personId, personService, profileMapper)
}