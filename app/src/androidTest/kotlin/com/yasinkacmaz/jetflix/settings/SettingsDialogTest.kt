package com.yasinkacmaz.jetflix.settings

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.settings.Language
import com.yasinkacmaz.jetflix.ui.settings.SettingsContent
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class SettingsDialogTest {

    private val defaultLanguage = Language("Turkish", "tr", "")

    @Test
    fun should_render_loading_when_uiState_showLoading_is_true() = runComposeUiTest {
        val uiState = SettingsViewModel.UiState(showLoading = true)

        showSettingsDialog(uiState, defaultLanguage)

        onNodeWithText(getString(R.string.fetching_languages), useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun should_render_selected_language() = runComposeUiTest {
        val uiState = SettingsViewModel.UiState()

        showSettingsDialog(uiState, defaultLanguage)

        onNodeWithText(defaultLanguage.englishName, substring = true, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun should_render_languages_when_selected_language_clicked() = runComposeUiTest {
        val firstLanguageName = "English"
        val secondLanguageName = "Russian"
        val thirdLanguageName = "Chinese"
        val languages = listOf(
            Language(englishName = firstLanguageName, iso6391 = "en", ""),
            Language(englishName = secondLanguageName, iso6391 = "ru", ""),
            Language(englishName = thirdLanguageName, iso6391 = "ch", ""),
        )
        val uiState = SettingsViewModel.UiState(languages = languages)

        showSettingsDialog(uiState, defaultLanguage)
        onNodeWithText(defaultLanguage.englishName, substring = true, useUnmergedTree = true).performClick()

        languages.forEach {
            onNodeWithText(it.englishName, substring = true, useUnmergedTree = true).assertIsDisplayed()
        }
    }

    private fun ComposeUiTest.showSettingsDialog(uiState: SettingsViewModel.UiState, selectedLanguage: Language) =
        setTestContent {
            SettingsContent(
                uiState = uiState,
                selectedLanguage = selectedLanguage,
                onLanguageSelected = {},
                onDialogDismissed = {},
            )
        }
}
