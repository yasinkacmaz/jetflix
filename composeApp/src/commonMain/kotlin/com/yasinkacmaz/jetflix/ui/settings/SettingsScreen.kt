package com.yasinkacmaz.jetflix.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalNavigator
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.JetflixImage
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.app_name
import jetflix.composeapp.generated.resources.app_version
import jetflix.composeapp.generated.resources.back
import jetflix.composeapp.generated.resources.fetching_languages
import jetflix.composeapp.generated.resources.ic_jetflix
import jetflix.composeapp.generated.resources.language
import jetflix.composeapp.generated.resources.theme
import jetflix.composeapp.generated.resources.theme_dark
import jetflix.composeapp.generated.resources.theme_light
import jetflix.composeapp.generated.resources.theme_system_default
import jetflix.composeapp.generated.resources.title_settings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel = koinViewModel()) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    SettingsScreenContent(
        uiState = uiState,
        onBackClicked = { navigator.navigateUp() },
        onLanguageSelected = settingsViewModel::onLanguageSelected,
        onThemePreferenceSelected = settingsViewModel::onThemePreferenceSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    uiState: SettingsViewModel.UiState,
    onBackClicked: () -> Unit = {},
    onLanguageSelected: (Language) -> Unit = {},
    onThemePreferenceSelected: (ThemePreference) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        if (uiState.showLoading) {
            Loading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                title = stringResource(Res.string.fetching_languages),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .navigationBarsPadding()
                    .verticalScroll(scrollState)
                    .padding(MaterialTheme.spacing.l),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_jetflix),
                    contentDescription = stringResource(Res.string.app_name),
                    tint = MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.xl))

                LanguageSelector(
                    selectedLanguage = uiState.selectedLanguage,
                    languages = uiState.languages,
                    onLanguageSelected = onLanguageSelected,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.l))

                ThemeSelector(
                    currentPreference = uiState.themePreference,
                    onPreferenceSelected = onThemePreferenceSelected,
                )
            }
        }
    }
}

@Composable
private fun LanguageSelector(
    selectedLanguage: Language,
    languages: List<Language>,
    onLanguageSelected: (Language) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(text = stringResource(Res.string.language), style = MaterialTheme.typography.bodyLarge)
        }

        Box {
            OutlinedCard(
                onClick = { expanded = !expanded },
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.requiredWidth(IntrinsicSize.Min).widthIn(min = 180.dp),
            ) {
                Row(
                    modifier = Modifier.padding(MaterialTheme.spacing.m),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (selectedLanguage.flagUrl.isNotEmpty()) {
                        JetflixImage(
                            data = selectedLanguage.flagUrl,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Text(
                        text = selectedLanguage.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languages.forEach { language ->
                    val selected = language.iso6391 == selectedLanguage.iso6391
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (language.flagUrl.isNotEmpty()) {
                                    JetflixImage(
                                        data = language.flagUrl,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                                Text(language.displayName)
                            }
                        },
                        onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        },
                        trailingIcon = if (selected) {
                            { Icon(Icons.Default.Done, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeSelector(currentPreference: ThemePreference, onPreferenceSelected: (ThemePreference) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
        ) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(text = stringResource(Res.string.theme), style = MaterialTheme.typography.bodyLarge)
        }

        Box {
            OutlinedCard(
                onClick = { expanded = !expanded },
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.requiredWidth(IntrinsicSize.Min).widthIn(min = 180.dp),
            ) {
                Row(
                    modifier = Modifier.padding(MaterialTheme.spacing.m),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = themePreferenceLabel(currentPreference),
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ThemePreference.entries.forEach { preference ->
                    DropdownMenuItem(
                        text = { Text(themePreferenceLabel(preference)) },
                        onClick = {
                            onPreferenceSelected(preference)
                            expanded = false
                        },
                        trailingIcon = if (preference == currentPreference) {
                            {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun themePreferenceLabel(preference: ThemePreference): String = when (preference) {
    ThemePreference.SYSTEM_DEFAULT -> stringResource(Res.string.theme_system_default)
    ThemePreference.LIGHT -> stringResource(Res.string.theme_light)
    ThemePreference.DARK -> stringResource(Res.string.theme_dark)
}
