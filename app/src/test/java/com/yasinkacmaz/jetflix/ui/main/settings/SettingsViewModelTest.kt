package com.yasinkacmaz.jetflix.ui.main.settings

import com.yasinkacmaz.jetflix.service.ConfigurationService
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SettingsViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var configurationService: ConfigurationService

    @RelaxedMockK
    private lateinit var languageDataStore: LanguageDataStore

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        settingsViewModel = spyk(SettingsViewModel(configurationService, languageDataStore))
    }

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
