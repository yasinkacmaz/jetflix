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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.JetflixImage
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.fetching_languages
import jetflix.composeapp.generated.resources.flag_content_description
import jetflix.composeapp.generated.resources.language
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsDialog(settingsViewModel: SettingsViewModel = koinViewModel(), onDialogDismissed: () -> Unit) {
    val uiState = settingsViewModel.uiState.collectAsState().value
    Dialog(onDismissRequest = onDialogDismissed) {
        SettingsDialogContent(uiState, settingsViewModel::onLanguageSelected)
    }
}

@Composable
fun SettingsDialogContent(uiState: SettingsViewModel.UiState, onLanguageSelected: (Language) -> Unit) {
    Card {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.m), verticalArrangement = Arrangement.Center) {
            if (uiState.showLoading) {
                Loading(modifier = Modifier.fillMaxWidth(), title = stringResource(Res.string.fetching_languages))
            } else {
                LanguageRow(uiState = uiState, onLanguageSelected = onLanguageSelected)
            }
        }
    }
}

@Composable
private fun LanguageRow(uiState: SettingsViewModel.UiState, onLanguageSelected: (Language) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var showDropdown by remember { mutableStateOf(false) }
        Text(stringResource(Res.string.language))
        DropdownMenu(
            expanded = showDropdown,
            modifier = Modifier.fillMaxHeight(0.5f),
            onDismissRequest = { showDropdown = false },
        ) {
            uiState.languages.forEach { language ->
                val selected = language.iso6391 == uiState.selectedLanguage.iso6391
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
            uiState.selectedLanguage.displayName,
            uiState.selectedLanguage.flagUrl,
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
            JetflixImage(
                data = flagUrl,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(Res.string.flag_content_description, countryName),
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
