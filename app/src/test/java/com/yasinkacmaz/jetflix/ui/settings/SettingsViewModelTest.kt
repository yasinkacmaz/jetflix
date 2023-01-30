package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.util.test
import com.yasinkacmaz.jetflix.util.testDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.io.IOException

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private val configurationService: ConfigurationService = mockk(relaxed = true)
    private val languageDataStore: LanguageDataStore = mockk(relaxed = true)

    @Test
    fun `Should sort languages by englishName when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        coEvery { configurationService.fetchLanguages() } returns languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()
        settingsViewModel.fetchLanguages()

        coVerify { configurationService.fetchLanguages() }
        expectThat(uiStates[uiStates.lastIndex - 1]).isEqualTo(SettingsViewModel.UiState(showLoading = true))
        val sortedLanguages = listOf(Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        expectThat(uiStates.last()).isEqualTo(SettingsViewModel.UiState(showLoading = false, sortedLanguages))
    }

    @Test
    fun `Should create state with empty languages when fetch languages fails`() = runTest {
        coEvery { configurationService.fetchLanguages() } throws IOException()

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()
        settingsViewModel.fetchLanguages()

        coVerify { configurationService.fetchLanguages() }

        expectThat(uiStates[uiStates.lastIndex - 1]).isEqualTo(SettingsViewModel.UiState(showLoading = true))
        expectThat(uiStates.last()).isEqualTo(SettingsViewModel.UiState(showLoading = false))
    }

    @Test
    fun `Should call language data store when language selected`() = runTest {
        val settingsViewModel = createViewModel()

        settingsViewModel.onLanguageSelected(Language.default)

        coVerify { languageDataStore.onLanguageSelected(Language.default) }
    }

    private fun createViewModel() = SettingsViewModel(configurationService, languageDataStore, testDispatchers)
}
