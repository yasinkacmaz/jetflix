package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animate
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesContent
import com.yasinkacmaz.jetflix.util.modifier.gradientBackground
import com.yasinkacmaz.jetflix.util.modifier.gradientBorder
import com.yasinkacmaz.jetflix.util.statusBarsPadding

@Composable
fun GenresScreen(genreUiModels: List<GenreUiModel>, isDarkTheme: MutableState<Boolean>) {
    val selectedGenre = SelectedGenreAmbient.current
    Surface(modifier = Modifier.fillMaxSize(), elevation = 0.dp) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Surface(modifier = Modifier.fillMaxWidth().wrapContentHeight(), elevation = 8.dp) {
                Column(Modifier.fillMaxWidth()) {
                    TopAppBar(isDarkTheme)
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
fun TopAppBar(isDarkTheme: MutableState<Boolean>) {
    val tint = animate(if (isDarkTheme.value) MaterialTheme.colors.onSurface else MaterialTheme.colors.primary)
    TopAppBar(
        title = {
            Icon(asset = vectorResource(id = R.drawable.ic_jetflix), tint = tint, modifier = Modifier.size(90.dp))
        },
        actions = {
            Switch(checked = isDarkTheme.value, onCheckedChange = { isDarkTheme.value = it })
        },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.surface
    )
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
    val selectedGenre = SelectedGenreAmbient.current
    val selected = selectedGenre.value == genreUiModel
    val colors = listOf(genreUiModel.primaryColor, genreUiModel.secondaryColor)
    val shape = RoundedCornerShape(percent = 50)
    val modifier = Modifier
        .gradientBorder(colors, shape, 2.dp, selected)
        .gradientBackground(colors, shape = shape, selected)
        .clickable(onClick = {
            if (!selected) {
                selectedGenre.value = genreUiModel
            }
        })
        .padding(horizontal = 8.dp, vertical = 2.dp)

    Text(text = genreUiModel.genre.name, style = MaterialTheme.typography.body2, modifier = modifier)
}
