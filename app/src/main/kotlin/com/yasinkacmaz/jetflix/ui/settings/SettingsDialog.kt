package com.yasinkacmaz.jetflix.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.theme.spacing
import org.koin.compose.viewmodel.koinViewModel

const val SETTINGS_DIALOG_TAG = "SettingsDialog"
private const val FLAG_ID = "flag"
private const val TICK_ID = "tickIcon"
private const val DROPDOWN_ID = "dropdownIcon"

private val placeholder = Placeholder(
    width = 2.5.em,
    height = 1.5.em,
    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
)

@Composable
fun SettingsDialog(onDialogDismissed: () -> Unit) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    LaunchedEffect(Unit) {
        settingsViewModel.fetchLanguages()
    }
    val uiState = settingsViewModel.uiState.collectAsState().value
    val selectedLanguage = settingsViewModel.selectedLanguage.collectAsState(initial = Language.default).value
    SettingsContent(uiState, selectedLanguage, settingsViewModel::onLanguageSelected, onDialogDismissed)
}

@Composable
fun SettingsContent(
    uiState: SettingsViewModel.UiState,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDialogDismissed: () -> Unit,
) = Dialog(onDismissRequest = onDialogDismissed) {
    Card(modifier = Modifier.semantics { testTag = SETTINGS_DIALOG_TAG }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.l),
            verticalArrangement = Arrangement.Center,
        ) {
            if (uiState.showLoading) {
                LoadingRow(title = stringResource(R.string.fetching_languages))
            } else {
                LanguageRow(
                    uiState.languages,
                    selectedLanguage,
                    onLanguageSelected = {
                        onLanguageSelected(it)
                        onDialogDismissed()
                    },
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    languages: List<Language>,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var showDropdown by remember { mutableStateOf(false) }
        Text(stringResource(R.string.language))
        DropdownMenu(
            expanded = showDropdown,
            modifier = Modifier.fillMaxHeight(0.5f),
            onDismissRequest = { showDropdown = false },
        ) {
            languages.forEach { language ->
                val selected = language == selectedLanguage
                DropdownItem(language.englishName, language.flagUrl, selected) {
                    onLanguageSelected(language)
                    showDropdown = false
                }
            }
        }
        ToggleContent(selectedLanguage.englishName, selectedLanguage.flagUrl) {
            showDropdown = true
        }
    }
}

@Composable
private fun ToggleContent(countryName: String, flagUrl: String, onClick: () -> Unit) {
    val flagContent = flagContent(flagUrl, countryName)
    val arrowContent = iconContent(DROPDOWN_ID, Icons.Default.ArrowDropDown)
    Text(
        text = buildAnnotatedString {
            appendInlineContent(FLAG_ID)
            append("  $countryName")
            appendInlineContent(DROPDOWN_ID)
        },
        inlineContent = mapOf(arrowContent, flagContent),
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun DropdownItem(countryName: String, flagUrl: String, selected: Boolean, onClick: () -> Unit) {
    DropdownMenuItem(
        enabled = !selected,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = buildAnnotatedString {
                    if (selected) {
                        appendInlineContent(TICK_ID)
                    }
                    appendInlineContent(FLAG_ID)
                    append("  $countryName")
                },
                style = MaterialTheme.typography.labelMedium,
                inlineContent = inlineContent(flagUrl, countryName, selected),
            )
        },
    )
}

private fun inlineContent(flagUrl: String, countryName: String, selected: Boolean): Map<String, InlineTextContent> {
    val flagContent = flagContent(flagUrl, countryName)
    return if (selected) mapOf(iconContent(TICK_ID, Icons.Default.Done), flagContent) else mapOf(flagContent)
}

private fun iconContent(id: String, icon: ImageVector) = id to InlineTextContent(
    placeholder = placeholder,
    children = {
        Image(
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
        )
    },
)

private fun flagContent(flagUrl: String, countryName: String) = FLAG_ID to InlineTextContent(
    placeholder = placeholder,
    children = {
        AsyncImage(
            model = flagUrl,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
            contentDescription = stringResource(id = R.string.flag_content_description, countryName),
        )
    },
)
