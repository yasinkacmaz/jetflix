package com.yasinkacmaz.jetflix.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingRow
import com.yasinkacmaz.jetflix.ui.theme.spacing
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsDialog(onDialogDismissed: () -> Unit) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    LaunchedEffect(Unit) {
        settingsViewModel.fetchLanguages()
    }
    val uiState = settingsViewModel.uiState.collectAsState().value
    val selectedLanguage = settingsViewModel.selectedLanguage.collectAsState(initial = Language.default)
    SettingsContent(uiState, selectedLanguage.value, settingsViewModel::onLanguageSelected, onDialogDismissed)
}

@Composable
fun SettingsContent(
    uiState: SettingsViewModel.UiState,
    selectedLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDialogDismissed: () -> Unit,
) = Dialog(onDismissRequest = onDialogDismissed) {
    Card {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.m), verticalArrangement = Arrangement.Center) {
            if (uiState.showLoading) {
                LoadingRow(title = stringResource(R.string.fetching_languages))
            } else {
                LanguageRow(uiState.languages, selectedLanguage, onLanguageSelected = { onLanguageSelected(it) })
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
                val selected = language.iso6391 == selectedLanguage.iso6391
                DropdownItem(
                    countryName = language.displayName,
                    flagUrl = language.flagUrl,
                    trailingIcon = if (selected) Icons.Default.Done else null,
                    selected = selected,
                ) {
                    onLanguageSelected(language)
                    showDropdown = false
                }
            }
        }
        DropdownItem(
            selectedLanguage.displayName,
            selectedLanguage.flagUrl,
            trailingIcon = Icons.Default.ArrowDropDown,
        ) { showDropdown = true }
    }
}

@Composable
private fun DropdownItem(
    countryName: String,
    flagUrl: String,
    selected: Boolean = false,
    trailingIcon: ImageVector?,
    onClick: () -> Unit = {},
) {
    DropdownMenuItem(
        enabled = !selected,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        text = { Text(countryName) },
        leadingIcon = {
            AsyncImage(
                model = flagUrl,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(id = R.string.flag_content_description, countryName),
            )
        },
        trailingIcon = {
            trailingIcon?.let {
                Image(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        },
    )
}
