@file:OptIn(ExperimentalMaterialApi::class)

package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.filter.FilterViewModel
import com.yasinkacmaz.jetflix.ui.main.filter.toFilterOptions
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesContent
import com.yasinkacmaz.jetflix.util.modifier.gradientBackground
import com.yasinkacmaz.jetflix.util.modifier.gradientBorder
import com.yasinkacmaz.jetflix.util.toggle
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun GenresScreen(
    genreUiModels: List<GenreUiModel>,
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>
) {
    val filterViewModel = viewModel<FilterViewModel>()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            BottomSheetContent(filterViewModel, sheetState)
        },
        content = {
            GenreContent(genreUiModels, isDarkTheme, showSettingsDialog, sheetState)
        }
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun BottomSheetContent(filterViewModel: FilterViewModel, bottomSheetState: ModalBottomSheetState) {
    Surface(
        Modifier.fillMaxWidth(),
        elevation = 8.dp,
        color = MaterialTheme.colors.primary
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { bottomSheetState.hide() }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_content_description)
                    )
                }
                Text(
                    text = stringResource(id = R.string.title_filter_bottom_sheet),
                    color = MaterialTheme.colors.onPrimary
                )
            }
            Text(
                stringResource(id = R.string.reset),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.clickable { filterViewModel.onResetClicked() }
            )
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 16.dp)
    ) {
        val filterState = filterViewModel.filterState.collectAsState().value
        filterState.toFilterOptions().forEach { filterOption ->
            filterOption.Render() {
                val newState = filterOption.modifyFilterState(filterState)
                filterViewModel.onFilterStateChanged(newState)
            }
        }
    }
}

@Composable
private fun GenreContent(
    genreUiModels: List<GenreUiModel>,
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
                    GenreChips(genreUiModels)
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
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = LocalSelectedGenre.current.value
            ) { selectedGenre ->
                MoviesContent(selectedGenre.genre)
            }
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

@Composable
fun GenreChips(genreUiModels: List<GenreUiModel>) {
    val space = 8.dp
    Row(
        Modifier
            .background(MaterialTheme.colors.surface)
            .horizontalScroll(rememberScrollState())
            .padding(start = space)
            .padding(vertical = space * 1.5f)
    ) {
        genreUiModels.forEach { genreUiModel ->
            GenreChip(genreUiModel)
            Spacer(modifier = Modifier.width(space))
        }
    }
}

@Composable
fun GenreChip(genreUiModel: GenreUiModel) {
    val selectedGenre = LocalSelectedGenre.current
    val selected = selectedGenre.value == genreUiModel
    val colors = listOf(genreUiModel.primaryColor, genreUiModel.secondaryColor)
    val shape = RoundedCornerShape(percent = 50)
    val scale = animateFloatAsState(if (selected) 1.1f else 1f).value
    val modifier = Modifier
        .scale(scale)
        .shadow(animateDpAsState(if (selected) 8.dp else 4.dp).value, shape)
        .background(MaterialTheme.colors.surface)
        .gradientBorder(colors, shape, 2.dp, selected)
        .gradientBackground(colors, shape = shape, selected)
        .clickable(onClick = { if (!selected) selectedGenre.value = genreUiModel })
        .padding(horizontal = 8.dp, vertical = 2.dp)

    Text(text = genreUiModel.genre.name.orEmpty(), style = MaterialTheme.typography.body2, modifier = modifier)
}
