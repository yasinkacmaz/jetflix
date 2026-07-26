package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.PlatformInfo
import com.yasinkacmaz.jetflix.util.ViewModelTest
import com.yasinkacmaz.jetflix.util.client.FakeConfigurationClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException

class SettingsViewModelTest : ViewModelTest() {

    private val configurationService = FakeConfigurationClient()
    private val languageDataStore = LanguageDataStore(json, FakeStringDataStore())
    private val themeDataStore = ThemeDataStore(FakeStringDataStore())
    private val platformInfo = PlatformInfo(appVersionName = "2.0.0")

    @Test
    fun `Should sort languages by englishName when fetch languages succeed`() = runTest {
        val languages = listOf(Language(englishName = "2", "", ""), Language(englishName = "1", "", ""))
        configurationService.languages = languages

        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        val sortedLanguages =
            listOf(Language.default, Language(englishName = "1", "", ""), Language(englishName = "2", "", ""))
        uiStates.last() shouldBe
            SettingsViewModel.UiState(showLoading = false, languages = sortedLanguages, versionName = "2.0.0")
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

        uiStates.last() shouldBe SettingsViewModel.UiState(showLoading = false, versionName = "2.0.0")
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

    @Test
    fun `Should update theme preference when preference selected`() = runTest {
        val settingsViewModel = createViewModel()

        settingsViewModel.onThemePreferenceSelected(ThemePreference.DARK)

        themeDataStore.themePreference.test().last() shouldBe ThemePreference.DARK
    }

    @Test
    fun `Should update ui state when theme preference updated`() = runTest {
        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        settingsViewModel.onThemePreferenceSelected(ThemePreference.LIGHT)

        uiStates.last().themePreference shouldBe ThemePreference.LIGHT
    }

    @Test
    fun `Should initialize state with version name from platform info`() = runTest {
        val settingsViewModel = createViewModel()
        val uiStates = settingsViewModel.uiState.test()

        uiStates.last().versionName shouldBe "2.0.0"
    }

    private fun createViewModel() =
        SettingsViewModel(configurationService, languageDataStore, themeDataStore, platformInfo)
}
