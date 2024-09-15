package com.yasinkacmaz.jetflix.ui.movies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ColorFilter
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
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 16.dp) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = 2.dp),
                ) {
                    JetflixAppBar(onSettingsClicked = { showSettingsDialog = true })
                    SearchBar(searchQuery, moviesViewModel::onSearch)
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = searchQuery.value.isBlank()) {
                FloatingActionButton(
                    modifier = Modifier
                        .wrapContentSize()
                        .navigationBarsPadding(),
                    onClick = { openBottomSheet = !openBottomSheet },
                    content = {
                        val color = if (LocalDarkTheme.current.value) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                        val tint = animateColorAsState(color, label = "fabTint").value
                        Image(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(id = R.string.title_filter_bottom_sheet),
                            colorFilter = ColorFilter.tint(tint),
                        )
                    },
                )
            }
        },
        content = { contentPadding ->
            MoviesGrid(Modifier.padding(contentPadding), moviesViewModel)
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
    val colors = MaterialTheme.colorScheme
    val isDarkTheme = LocalDarkTheme.current
    val iconTint =
        animateColorAsState(if (isDarkTheme.value) colors.onSurface else colors.primary, label = "appIconTint").value
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp),
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
            modifier = Modifier.size(82.dp),
        )

        val icon = if (isDarkTheme.value) Icons.Default.NightsStay else Icons.Default.WbSunny
        IconButton(onClick = { isDarkTheme.value = !isDarkTheme.value }) {
            val contentDescriptionResId = if (isDarkTheme.value) {
                R.string.light_theme_content_description
            } else {
                R.string.dark_theme_content_description
            }
            Icon(icon, contentDescription = stringResource(id = contentDescriptionResId), tint = iconTint)
        }
    }
}

@Composable
private fun SearchBar(searchQuery: MutableState<String>, onSearch: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .heightIn(max = 50.dp)
            .fillMaxWidth(),
        value = searchQuery.value,
        textStyle = MaterialTheme.typography.titleMedium,
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
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
        ),
    )
}
