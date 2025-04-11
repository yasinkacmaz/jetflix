package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.ViewModelTest
import com.yasinkacmaz.jetflix.util.client.FakeConfigurationClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test

class SettingsViewModelTest: ViewModelTest() {

    private val configurationService = FakeConfigurationClient()
    private val languageDataStore = LanguageDataStore(json, FakeStringDataStore())

    @Test
    fun `Should sort languages by englishName when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        configurationService.languages = languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        val sortedLanguages =
            listOf(Language.default, Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        uiStates.last() shouldBe SettingsViewModel.UiState(showLoading = false, sortedLanguages)
    }

    @Test
    fun `Should move default language to the first position when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "1", "", ""), Language.default)
        configurationService.languages = languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        uiStates.last().languages.first() shouldBe Language.default
        uiStates.last().languages.count { it == Language.default } shouldBe 1
    }

    @Test
    fun `Should create state with empty languages when fetch languages fails`() = runTest {
        configurationService.fetchLanguagesException = IOException()

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        uiStates.last() shouldBe SettingsViewModel.UiState(showLoading = false)
    }

    @Test
    fun `Should update ui state when language selected`() = runTest {
        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        val language = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
        settingsViewModel.onLanguageSelected(language)

        uiStates.last().selectedLanguage shouldBe language
    }

    @Test
    fun `Should update language data store when language selected`() = runTest {
        val settingsViewModel = createViewModel()

        val language = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
        settingsViewModel.onLanguageSelected(language)

        languageDataStore.language.test().last() shouldBe language
    }

    private fun createViewModel() = SettingsViewModel(configurationService, languageDataStore)
}
