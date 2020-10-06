package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.main.movies.MoviesContent

@Composable
fun GenresContent(genres: List<Genre>, selectedGenre: MutableState<Genre>, isDarkTheme: MutableState<Boolean>) {
    Scaffold(
        topBar = { TopAppBar(isDarkTheme.value) { isDarkTheme.value = it } },
        bottomBar = { BottomNavigation(genres, selectedGenre) },
        bodyContent = { paddingValues ->
            Crossfade(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                current = selectedGenre.value
            ) { genre ->
                MoviesContent(genre)
            }
        }
    )
}

@Composable
fun TopAppBar(initialTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    TopAppBar(
        title = {
            Icon(asset = vectorResource(id = R.drawable.ic_jetflix), modifier = Modifier.size(100.dp))
        },
        actions = {
            ChangeTheme(initialTheme, onThemeChanged)
        }
    )
}

@Composable
private fun ChangeTheme(initialTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    Text(text = stringResource(id = R.string.change_theme))
    Switch(checked = initialTheme, onCheckedChange = onThemeChanged)
}

@Composable
fun BottomNavigation(genres: List<Genre>, selectedGenre: MutableState<Genre>) {
    BottomNavigation {
        genres.forEach { genre ->
            val selected = genre == selectedGenre.value
            BottomNavigationItem(
                label = { Text(genre.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                icon = { Icon(Icons.Default.Movie) },
                selected = selected,
                onClick = {
                    selectedGenre.value = genre
                }
            )
        }
    }
}

@Preview
@Composable
private fun GenresContentPreview() {
    val genre = Genre(1, "Action")
    GenresContent(genres = listOf(genre), mutableStateOf(genre), mutableStateOf(false))
}
