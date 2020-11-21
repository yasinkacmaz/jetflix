package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animate
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.settings.SettingsDialog
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesContent
import com.yasinkacmaz.jetflix.util.modifier.gradientBackground
import com.yasinkacmaz.jetflix.util.modifier.gradientBorder
import com.yasinkacmaz.jetflix.util.statusBarsPadding
import com.yasinkacmaz.jetflix.util.toggle

@Composable
fun GenresScreen(genreUiModels: List<GenreUiModel>, isDarkTheme: MutableState<Boolean>) {
    val selectedGenre = SelectedGenreAmbient.current
    Surface(modifier = Modifier.fillMaxSize(), elevation = 0.dp) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight(), elevation = 16.dp) {
                Column(Modifier.fillMaxWidth()) {
                    JetflixAppBar(isDarkTheme)
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
private fun JetflixAppBar(isDarkTheme: MutableState<Boolean>) {
    val tint = animate(if (isDarkTheme.value) MaterialTheme.colors.onSurface else MaterialTheme.colors.primary)
    val showSettingsDialog = remember { mutableStateOf(false) }
    Row(
        Modifier.background(MaterialTheme.colors.surface).fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { showSettingsDialog.value = true }) {
            Icon(Icons.Default.Settings, tint = tint)
        }

        Icon(asset = vectorResource(id = R.drawable.ic_jetflix), tint = tint, modifier = Modifier.size(90.dp))

        val icon = if (isDarkTheme.value) Icons.Default.NightsStay else Icons.Default.WbSunny
        IconButton(onClick = isDarkTheme::toggle) {
            Icon(icon, tint = tint)
        }
    }

    if (showSettingsDialog.value) {
        SettingsDialog() {
            showSettingsDialog.value = false
        }
    }
}

@Composable
fun GenreChips(genreUiModels: List<GenreUiModel>) {
    val space = 8.dp
    Row(
        Modifier.background(MaterialTheme.colors.surface)
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
    val selectedGenre = SelectedGenreAmbient.current
    val selected = selectedGenre.value == genreUiModel
    val colors = listOf(genreUiModel.primaryColor, genreUiModel.secondaryColor)
    val shape = RoundedCornerShape(percent = 50)
    val scale = animate(if (selected) 1.1f else 1f)
    val modifier = Modifier
        .drawLayer(scaleX = scale, scaleY = scale)
        .drawShadow(animate(if (selected) 8.dp else 4.dp), shape)
        .background(MaterialTheme.colors.surface)
        .gradientBorder(colors, shape, 2.dp, selected)
        .gradientBackground(colors, shape = shape, selected)
        .clickable(onClick = { if (!selected) selectedGenre.value = genreUiModel })
        .padding(horizontal = 8.dp, vertical = 2.dp)

    Text(text = genreUiModel.genre.name, style = MaterialTheme.typography.body2, modifier = modifier)
}
