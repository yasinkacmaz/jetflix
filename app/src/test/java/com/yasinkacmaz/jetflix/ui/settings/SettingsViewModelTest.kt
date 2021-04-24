package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.mockkRelaxed
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SettingsViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val configurationService: ConfigurationService = mockkRelaxed()

    private val languageDataStore: LanguageDataStore = mockkRelaxed()

    private val settingsViewModel = spyk(SettingsViewModel(configurationService, languageDataStore))

    @Test
    fun `fetchLanguages success`() = coroutineTestRule.runBlockingTest {
        val languages = listOf(Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        coEvery { configurationService.fetchLanguages() } returns languages

        settingsViewModel.fetchLanguages()

        coVerify { configurationService.fetchLanguages() }
        verifyOrder {
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = true)
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = false, languages)
        }
    }

    @Test
    fun `fetchLanguages should sort languages by englishName when success`() = coroutineTestRule.runBlockingTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        coEvery { configurationService.fetchLanguages() } returns languages

        settingsViewModel.fetchLanguages()

        coVerify { configurationService.fetchLanguages() }
        verifyOrder {
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = true)
            val sortedLanguages = listOf(Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = false, sortedLanguages)
        }
    }

    @Test
    fun `fetchLanguages error`() = coroutineTestRule.runBlockingTest {
        coEvery { configurationService.fetchLanguages() } throws IOException()

        settingsViewModel.fetchLanguages()

        coVerify { configurationService.fetchLanguages() }
        verifyOrder {
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = true)
            settingsViewModel.uiValue = SettingsViewModel.UiState(showLoading = false)
        }
    }
}
