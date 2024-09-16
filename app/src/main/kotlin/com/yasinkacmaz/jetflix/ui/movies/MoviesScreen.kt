package com.yasinkacmaz.jetflix.ui.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheetContent
import com.yasinkacmaz.jetflix.ui.filter.FilterHeader
import com.yasinkacmaz.jetflix.ui.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.main.LocalDarkTheme
import com.yasinkacmaz.jetflix.ui.settings.SettingsContent
import com.yasinkacmaz.jetflix.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(moviesViewModel: MoviesViewModel, filterViewModel: FilterViewModel) {
    val sheetState = rememberModalBottomSheetState()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val filterState = filterViewModel.filterState.collectAsState().value
    val hideFilterBottomSheet: () -> Unit = { openBottomSheet = false }

    val searchQuery = remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(bottom = MaterialTheme.spacing.s),
            ) {
                JetflixAppBar(onSettingsClicked = { showSettingsDialog = true })
                JetflixSearchBar(searchQuery, moviesViewModel::onSearch)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = searchQuery.value.isBlank()) {
                FloatingActionButton(
                    onClick = { openBottomSheet = true },
                    content = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(id = R.string.title_filter_bottom_sheet),
                        )
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
        content = { contentPadding ->
            MoviesGrid(contentPadding, moviesViewModel)
            if (showSettingsDialog) {
                SettingsContent(onDialogDismissed = { showSettingsDialog = false })
            }
        },
    )

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scrimColor = Color.DarkGray.copy(alpha = 0.7f),
            content = {
                val onResetClicked = if (filterState == null) null else filterViewModel::onResetClicked
                FilterHeader(onHideClicked = hideFilterBottomSheet, onResetClicked = onResetClicked)

                if (filterState == null) {
                    LoadingColumn(
                        title = stringResource(id = R.string.loading_filter_options),
                        modifier = Modifier.fillMaxHeight(0.4f),
                    )
                } else {
                    FilterBottomSheetContent(
                        filterState = filterState,
                        onFilterStateChanged = filterViewModel::onFilterStateChanged,
                    )
                }
            },
        )
    }
}

@Composable
private fun JetflixAppBar(onSettingsClicked: () -> Unit) {
    var isDarkTheme by LocalDarkTheme.current
    val iconTint = animateColorAsState(
        if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
        label = "appIconTint",
    ).value
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onSettingsClicked) {
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.settings_content_description),
                tint = iconTint,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_jetflix),
            contentDescription = stringResource(id = R.string.app_name),
            tint = iconTint,
            modifier = Modifier.height(24.dp),
        )

        IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
            val contentDescriptionResId = if (isDarkTheme) {
                R.string.light_theme_content_description
            } else {
                R.string.dark_theme_content_description
            }
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.NightsStay else Icons.Default.WbSunny,
                contentDescription = stringResource(id = contentDescriptionResId),
                tint = iconTint,
            )
        }
    }
}

@Composable
private fun JetflixSearchBar(searchQuery: MutableState<String>, onSearch: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.s)
            .heightIn(max = 52.dp)
            .fillMaxWidth(),
        value = searchQuery.value,
        textStyle = MaterialTheme.typography.titleSmall,
        singleLine = true,
        shape = RoundedCornerShape(50),
        placeholder = { Text(stringResource(id = R.string.search_movies), color = Color.Gray) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.HighlightOff,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        searchQuery.value = ""
                        onSearch("")
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query ->
            searchQuery.value = query
            onSearch(query)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}
