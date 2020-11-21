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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.util.toggle

@Composable
fun SettingsDialog(onDialogDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDialogDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                LanguageRow()
                Spacer(modifier = Modifier.height(16.dp))
                CountryRow()
            }
        }
    }
}

@Composable
private fun LanguageRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(stringResource(R.string.language))
        val showLanguageDropdown = remember { mutableStateOf(false) }
        var selectedLanguage by remember { mutableStateOf("Turkish") }
        val languages = listOf("Turkish", "English", "Chinese", "Dutch")
        DropdownMenu(
            toggle = { ToggleContent(title = selectedLanguage, onClick = { showLanguageDropdown.toggle() }) },
            expanded = showLanguageDropdown.value,
            onDismissRequest = { showLanguageDropdown.value = false }
        ) {
            languages.forEach { language ->
                val selected = language == selectedLanguage
                DropdownItem(language, selected) {
                    selectedLanguage = language
                    showLanguageDropdown.value = false
                }
            }
        }
    }
}

@Composable
private fun CountryRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(stringResource(R.string.country))
        val showCountryDropdown = remember { mutableStateOf(false) }
        var selectedCountry by remember { mutableStateOf("Turkish") }
        val countries = listOf("Turkey", "England", "Germany", "China")
        DropdownMenu(
            toggle = { ToggleContent(title = selectedCountry, onClick = { showCountryDropdown.toggle() }) },
            expanded = showCountryDropdown.value,
            onDismissRequest = { showCountryDropdown.value = false }
        ) {
            countries.forEach { country ->
                val selected = country == selectedCountry
                DropdownItem(country, selected) {
                    selectedCountry = country
                    showCountryDropdown.value = false
                }
            }
        }
    }
}

@Composable
private fun ToggleContent(title: String, onClick: () -> Unit) {
    val dropdownArrowId = "dropdownIcon"
    val arrowContent = InlineTextContent(
        placeholder = Placeholder(
            width = 2.em,
            height = 1.em,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
        ),
        children = {
            Image(asset = Icons.Default.ArrowDropDown, colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface))
        }
    )
    Text(
        text = annotatedString {
            append(title)
            appendInlineContent(dropdownArrowId)
        },
        inlineContent = mapOf(dropdownArrowId to arrowContent),
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
private fun DropdownItem(text: String, selected: Boolean, onClick: () -> Unit) {
    DropdownMenuItem(enabled = !selected, onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        val tickIconId = "tickIcon"
        val tickIconContent = InlineTextContent(
            placeholder = Placeholder(
                width = 2.em,
                height = 1.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
            ),
            children = {
                Image(asset = Icons.Default.Done, colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface))
            }
        )
        Text(
            text = annotatedString {
                if (selected) {
                    appendInlineContent(tickIconId)
                }
                append(text)
            },
            inlineContent = if (selected) mapOf(tickIconId to tickIconContent) else emptyMap()
        )
    }
}
