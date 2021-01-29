package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesContent
import com.yasinkacmaz.jetflix.util.modifier.gradientBackground
import com.yasinkacmaz.jetflix.util.modifier.gradientBorder
import com.yasinkacmaz.jetflix.util.statusBarsPadding
import com.yasinkacmaz.jetflix.util.toggle

@Composable
fun GenresScreen(
    genreUiModels: List<GenreUiModel>,
    isDarkTheme: MutableState<Boolean>,
    showSettingsDialog: MutableState<Boolean>
) {
    val selectedGenre = AmbientSelectedGenre.current
    Surface(modifier = Modifier.fillMaxSize(), elevation = 0.dp) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
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
            Crossfade(modifier = Modifier.fillMaxSize(), current = selectedGenre.value) { selectedGenre ->
                MoviesContent(selectedGenre.genre)
            }
        }
    }
}

@Composable
private fun JetflixAppBar(isDarkTheme: MutableState<Boolean>, showSettingsDialog: MutableState<Boolean>) {
    val tint = animateColorAsState(
        if (isDarkTheme.value) MaterialTheme.colors.onSurface else MaterialTheme.colors.primary
    ).value
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
            imageVector = vectorResource(id = R.drawable.ic_jetflix),
            contentDescription = null,
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
    val selectedGenre = AmbientSelectedGenre.current
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
