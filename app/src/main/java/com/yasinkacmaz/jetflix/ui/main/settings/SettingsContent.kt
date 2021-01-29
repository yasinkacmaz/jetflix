package com.yasinkacmaz.jetflix.ui.main.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.window.Dialog
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.util.toggle
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val SETTINGS_DIALOG_TAG = "SettingsDialog"

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SettingsContent(onDialogDismiss: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel()
    settingsViewModel.fetchLanguages()
    val uiState = settingsViewModel.uiState.collectAsState().value
    val selectedLanguage = settingsViewModel.selectedLanguage.collectAsState(initial = Language.default)
    SettingsDialog(
        uiState,
        selectedLanguage,
        onDialogDismiss = onDialogDismiss,
        onLanguageSelected = { settingsViewModel.onLanguageSelected(it) }
    )
}

@Composable
fun SettingsDialog(
    uiState: SettingsViewModel.UiState,
    selectedLanguage: State<Language>,
    onLanguageSelected: (Language) -> Unit,
    onDialogDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDialogDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { testTag = SETTINGS_DIALOG_TAG }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (uiState.showLoading) {
                    LoadingRow(title = stringResource(R.string.fetching_languages))
                } else {
                    LanguageRow(uiState.languages, selectedLanguage.value, onLanguageSelected)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun LanguageRow(
    languages: List<Language>,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(stringResource(R.string.language))
        val showLanguageDropdown = remember { mutableStateOf(false) }
        DropdownMenu(
            toggle = {
                ToggleContent(
                    countryName = selectedLanguage.englishName,
                    flagUrl = selectedLanguage.flagUrl,
                    onClick = { showLanguageDropdown.toggle() }
                )
            },
            expanded = showLanguageDropdown.value,
            onDismissRequest = { showLanguageDropdown.value = false }
        ) {
            languages.forEach { language ->
                val selected = language == selectedLanguage
                DropdownItem(language.englishName, language.flagUrl, selected) {
                    onLanguageSelected(language)
                    showLanguageDropdown.value = false
                }
            }
        }
    }
}

@Composable
private fun ToggleContent(countryName: String, flagUrl: String, onClick: () -> Unit) {
    val dropdownId = "dropdownIcon"
    val flagId = "flag"
    val flagContent = flagContent(flagId, flagUrl, countryName)
    val dropdownContent = iconContent(dropdownId, Icons.Default.ArrowDropDown)
    Text(
        text = buildAnnotatedString {
            appendInlineContent(flagId)
            append(countryName)
            appendInlineContent(dropdownId)
        },
        inlineContent = mapOf(dropdownContent, flagContent),
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun DropdownItem(countryName: String, flagUrl: String, selected: Boolean, onClick: () -> Unit) {
    DropdownMenuItem(enabled = !selected, onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        val tickIconId = "tickIcon"
        val flagId = "flag"
        val tickIconContent = iconContent(tickIconId, Icons.Default.Done)
        val flagContent = flagContent(flagId, flagUrl, countryName)
        Text(
            text = buildAnnotatedString {
                appendInlineContent(flagId)
                if (selected) {
                    appendInlineContent(tickIconId)
                }
                append(countryName)
            },
            inlineContent = if (selected) mapOf(tickIconContent, flagContent) else mapOf(flagContent)
        )
    }
}

private fun iconContent(id: String, icon: ImageVector) = id to InlineTextContent(
    placeholder = Placeholder(
        width = 2.em,
        height = 1.em,
        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
    ),
    children = {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
    }
)

private fun flagContent(flagId: String, flagUrl: String, countryName: String) = flagId to InlineTextContent(
    placeholder = Placeholder(
        width = 2.em,
        height = 1.em,
        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
    ),
    children = {
        CoilImage(
            data = flagUrl,
            contentDescription = stringResource(id = R.string.flag_content_description, countryName),
            Modifier.padding(end = 4.dp)
        )
    }
)
