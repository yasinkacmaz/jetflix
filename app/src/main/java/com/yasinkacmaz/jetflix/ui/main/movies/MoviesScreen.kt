package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.filter.FilterContent
import com.yasinkacmaz.jetflix.ui.main.filter.FilterViewModel
import com.yasinkacmaz.jetflix.util.toggle
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun MoviesScreen(
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>
) {
    val filterViewModel = viewModel<FilterViewModel>()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            val filterState = filterViewModel.filterState.collectAsState().value
            FilterContent(
                filterState,
                filterViewModel::onFilterStateChanged,
                filterViewModel::onResetClicked,
                onHideClicked = { sheetState.hide() }
            )
        },
        content = {
            MoviesGrid(isDarkTheme, showSettingsDialog, sheetState)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MoviesGrid(
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>,
    bottomSheetState: ModalBottomSheetState
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = 16.dp
            ) {
                Column(Modifier.fillMaxWidth()) {
                    JetflixAppBar(isDarkTheme, showSettingsDialog)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .wrapContentSize()
                    .navigationBarsPadding(),
                onClick = { bottomSheetState.show() },
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
        },
        bodyContent = {
            MoviesGrid()
        }
    )
}

@Composable
private fun JetflixAppBar(isDarkTheme: MutableState<Boolean>, showSettingsDialog: MutableState<Boolean>) {
    val colors = MaterialTheme.colors
    val tint = animateColorAsState(if (isDarkTheme.value) colors.onSurface else colors.primary).value
    Row(
        Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { showSettingsDialog.value = true }) {
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
            modifier = Modifier.size(90.dp)
        )

        val icon = if (isDarkTheme.value) Icons.Default.NightsStay else Icons.Default.WbSunny
        IconButton(onClick = isDarkTheme::toggle) {
            val contentDescriptionResId = if (isDarkTheme.value) {
                R.string.light_theme_content_description
            } else {
                R.string.dark_theme_content_description
            }
            Icon(icon, contentDescription = stringResource(id = contentDescriptionResId), tint = tint)
        }
    }
}
