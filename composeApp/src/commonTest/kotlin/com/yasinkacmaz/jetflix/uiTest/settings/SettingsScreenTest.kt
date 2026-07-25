package com.yasinkacmaz.jetflix.uiTest.settings

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.yasinkacmaz.jetflix.ui.settings.Language
import com.yasinkacmaz.jetflix.ui.settings.SettingsScreenContent
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.ui.settings.displayName
import com.yasinkacmaz.jetflix.uiTest.util.setTestContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    private val defaultLanguage = Language("Turkish", "tr", "Türkçe")

    @Test
    fun `Should render loading state`() = runComposeUiTest {
        val uiState = SettingsViewModel.UiState(showLoading = true)

        setTestContent {
            SettingsScreenContent(uiState = uiState)
        }

        onNodeWithText("Fetching Languages", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `Should render selected language`() = runComposeUiTest {
        val uiState = SettingsViewModel.UiState(selectedLanguage = defaultLanguage)

        setTestContent {
            SettingsScreenContent(uiState = uiState)
        }

        onNodeWithText(defaultLanguage.displayName, substring = true, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `Should render languages when selected language clicked`() = runComposeUiTest {
        val firstLanguageName = "English"
        val secondLanguageName = "Russian"
        val thirdLanguageName = "Chinese"
        val languages = listOf(
            Language(englishName = firstLanguageName, iso6391 = "en", ""),
            Language(englishName = secondLanguageName, iso6391 = "ru", ""),
            Language(englishName = thirdLanguageName, iso6391 = "ch", ""),
        )
        val uiState = SettingsViewModel.UiState(languages = languages, selectedLanguage = defaultLanguage)

        setTestContent {
            SettingsScreenContent(uiState = uiState)
        }
        onNodeWithText(defaultLanguage.displayName, substring = true, useUnmergedTree = true).performClick()

        languages.forEach {
            onNodeWithText(it.englishName, substring = true, useUnmergedTree = true).assertIsDisplayed()
        }
    }

    @Test
    fun `Should render app version`() = runComposeUiTest {
        val versionName = "2.0.0"
        val uiState = SettingsViewModel.UiState(versionName = versionName)

        setTestContent {
            SettingsScreenContent(uiState = uiState)
        }

        onNodeWithText("Version $versionName", substring = true, useUnmergedTree = true).assertIsDisplayed()
    }
}
