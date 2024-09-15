package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.client.FakeConfigurationClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import com.yasinkacmaz.jetflix.util.testDispatchers
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SettingsViewModelTest {

    private val configurationService = FakeConfigurationClient()
    private val languageDataStore = LanguageDataStore(json, FakeStringDataStore())

    @Test
    fun `Should sort languages by englishName when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        configurationService.languages = languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()
        settingsViewModel.fetchLanguages()

        expectThat(uiStates[uiStates.lastIndex - 1]).isEqualTo(SettingsViewModel.UiState(showLoading = true))
        val sortedLanguages = listOf(Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        expectThat(uiStates.last()).isEqualTo(SettingsViewModel.UiState(showLoading = false, sortedLanguages))
    }

    @Test
    fun `Should create state with empty languages when fetch languages fails`() = runTest {
        configurationService.fetchLanguagesException = IOException()

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()
        settingsViewModel.fetchLanguages()

        expectThat(uiStates[uiStates.lastIndex - 1]).isEqualTo(SettingsViewModel.UiState(showLoading = true))
        expectThat(uiStates.last()).isEqualTo(SettingsViewModel.UiState(showLoading = false))
    }

    @Test
    fun `Should call language data store when language selected`() = runTest {
        val settingsViewModel = createViewModel()

        val language = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
        settingsViewModel.onLanguageSelected(language)

        expectThat(languageDataStore.language.test().last()).isEqualTo(language)
    }

    private fun createViewModel() = SettingsViewModel(configurationService, languageDataStore, testDispatchers)
}
