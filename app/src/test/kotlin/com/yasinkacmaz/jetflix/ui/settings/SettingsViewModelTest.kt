package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.client.FakeConfigurationClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import com.yasinkacmaz.jetflix.util.testDispatchers
import io.kotest.matchers.shouldBe
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val configurationService = FakeConfigurationClient()
    private val languageDataStore = LanguageDataStore(json, FakeStringDataStore())

    @Test
    fun `Should sort languages by englishName when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        configurationService.languages = languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        val sortedLanguages = listOf(Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        uiStates.last() shouldBe SettingsViewModel.UiState(showLoading = false, sortedLanguages)
    }

    @Test
    fun `Should create state with empty languages when fetch languages fails`() = runTest {
        configurationService.fetchLanguagesException = IOException()

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        uiStates.last() shouldBe SettingsViewModel.UiState(showLoading = false)
    }

    @Test
    fun `Should update language data store when language selected`() = runTest {
        val settingsViewModel = createViewModel()

        val language = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
        settingsViewModel.onLanguageSelected(language)

        languageDataStore.language.test().last() shouldBe language
    }

    private fun createViewModel() = SettingsViewModel(configurationService, languageDataStore, testDispatchers)
}
