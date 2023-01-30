package com.yasinkacmaz.jetflix.ui.movies

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.filter.FilterBottomSheetContent
import com.yasinkacmaz.jetflix.ui.filter.FilterHeader
import com.yasinkacmaz.jetflix.ui.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.settings.SettingsContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoviesScreen(isDarkTheme: MutableState<Boolean>) {
    val filterViewModel = hiltViewModel<FilterViewModel>()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val filterState = filterViewModel.filterState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val hideFilterBottomSheet: () -> Unit = {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.DarkGray.copy(alpha = 0.7f),
        sheetContent = {
            val onResetClicked = if (filterState == null) null else filterViewModel::onResetClicked
            FilterHeader(onHideClicked = hideFilterBottomSheet, onResetClicked = onResetClicked)

            if (filterState == null) {
                LoadingColumn(
                    title = stringResource(id = R.string.loading_filter_options),
                    modifier = Modifier.fillMaxHeight(0.4f)
                )
            } else {
                FilterBottomSheetContent(
                    filterState = filterState,
                    onFilterStateChanged = filterViewModel::onFilterStateChanged
                )
            }
        },
        content = {
            MoviesGrid(isDarkTheme, sheetState)
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesGrid(
    isDarkTheme: MutableState<Boolean>,
    bottomSheetState: ModalBottomSheetState
) {
    val moviesViewModel = hiltViewModel<MoviesViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val searchQuery = remember { mutableStateOf("") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = Modifier.fillMaxWidth(), elevation = 16.dp) {
                Column(
                    Modifier
                        .background(MaterialTheme.colors.surface)
                        .padding(bottom = 2.dp)
                ) {
                    JetflixAppBar(isDarkTheme, onSettingsClicked = { showSettingsDialog = true })
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
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.show()
                        }
                    },
                    content = {
                        val color =
                            if (isDarkTheme.value) MaterialTheme.colors.surface else MaterialTheme.colors.onPrimary
                        val tint = animateColorAsState(color).value
                        Image(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(id = R.string.title_filter_bottom_sheet),
                            colorFilter = ColorFilter.tint(tint)
                        )
                    }
                )
            }
        },
        content = {
            MoviesGrid(moviesViewModel)
            if (showSettingsDialog) {
                SettingsContent(onDialogDismissed = { showSettingsDialog = false })
            }
        }
    )
}

@Composable
private fun JetflixAppBar(isDarkTheme: MutableState<Boolean>, onSettingsClicked: () -> Unit) {
    val colors = MaterialTheme.colors
    val tint = animateColorAsState(if (isDarkTheme.value) colors.onSurface else colors.primary).value
    Row(
        Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onSettingsClicked) {
            Icon(
                Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.settings_content_description),
                tint = tint
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_jetflix),
            contentDescription = stringResource(id = R.string.app_name),
            tint = tint,
            modifier = Modifier.size(82.dp)
        )

        val icon = if (isDarkTheme.value) Icons.Default.NightsStay else Icons.Default.WbSunny
        IconButton(onClick = { isDarkTheme.value = isDarkTheme.value.not() }) {
            val contentDescriptionResId = if (isDarkTheme.value) {
                R.string.light_theme_content_description
            } else {
                R.string.dark_theme_content_description
            }
            Icon(icon, contentDescription = stringResource(id = contentDescriptionResId), tint = tint)
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
        textStyle = MaterialTheme.typography.subtitle1,
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
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query ->
            searchQuery.value = query
            onSearch(query)
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colors.surface,
            unfocusedIndicatorColor = MaterialTheme.colors.surface
        )
    )
}
