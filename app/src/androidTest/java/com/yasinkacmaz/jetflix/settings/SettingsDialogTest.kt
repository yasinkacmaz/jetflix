package com.yasinkacmaz.jetflix.settings

import androidx.activity.ComponentActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.settings.Language
import com.yasinkacmaz.jetflix.ui.settings.SettingsDialog
import com.yasinkacmaz.jetflix.ui.settings.SettingsViewModel
import com.yasinkacmaz.jetflix.util.getString
import com.yasinkacmaz.jetflix.util.setTestContent
import org.junit.Rule
import org.junit.Test

class SettingsDialogTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val defaultLanguage = Language("Turkish", "tr", "")

    @Test
    fun should_render_loading_when_uiState_showLoading_is_true(): Unit = with(composeTestRule) {
        val selectedLanguage: State<Language> = mutableStateOf(defaultLanguage)
        val uiState = SettingsViewModel.UiState(showLoading = true)

        showSettingsDialog(uiState, selectedLanguage)

        onNodeWithText(getString(R.string.fetching_languages), useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun should_render_selected_language(): Unit = with(composeTestRule) {
        val selectedLanguage: State<Language> = mutableStateOf(defaultLanguage)
        val uiState = SettingsViewModel.UiState()

        showSettingsDialog(uiState, selectedLanguage)

        onNodeWithText(defaultLanguage.englishName, substring = true, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun should_render_languages_when_selected_language_clicked() = with(composeTestRule) {
        val selectedLanguage: State<Language> = mutableStateOf(defaultLanguage)
        val firstLanguageName = "English"
        val secondLanguageName = "Russian"
        val thirdLanguageName = "Chinese"
        val languages = listOf(
            Language(englishName = firstLanguageName, iso6391 = "en", ""),
            Language(englishName = secondLanguageName, iso6391 = "ru", ""),
            Language(englishName = thirdLanguageName, iso6391 = "ch", "")
        )
        val uiState = SettingsViewModel.UiState(languages = languages)

        showSettingsDialog(uiState, selectedLanguage)
        onNodeWithText(defaultLanguage.englishName, substring = true, useUnmergedTree = true).performClick()

        languages.forEach {
            onNodeWithText(it.englishName, substring = true, useUnmergedTree = true).assertIsDisplayed()
        }
    }

    private fun ComposeContentTestRule.showSettingsDialog(
        uiState: SettingsViewModel.UiState,
        selectedLanguage: State<Language>
    ) = setTestContent {
        SettingsDialog(
            onDialogDismiss = {},
            uiState = uiState,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = {}
        )
    }
}
