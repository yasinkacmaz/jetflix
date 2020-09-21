package com.yasinkacmaz.playground.ui.main.genres

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Normal
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Center
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.data.Genre
import com.yasinkacmaz.playground.ui.main.movies.MoviesContent
import com.yasinkacmaz.playground.ui.theme.PlayGroundTheme

@Composable
fun GenresContent(genres: List<Genre>) {
    val isDarkTheme = remember { mutableStateOf(false) }
    val selectedGenre = remember { mutableStateOf(genres.first()) }
    PlayGroundTheme(isDarkTheme = isDarkTheme.value) {
        Scaffold(
            topBar = {
                TopAppBar(isDarkTheme.value) {
                    isDarkTheme.value = it
                }
            },
            bottomBar = {
                BottomNavigation(genres, selectedGenre)
            },
            bodyContent = { paddingValues ->
                Crossfade(
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                    current = selectedGenre.value
                ) { genre ->
                    MoviesContent(genre)
                }
            })
    }
}

@Composable
fun TopAppBar(initialTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = AnnotatedString("Playground"),
                style = TextStyle(
                    textAlign = Center,
                    fontStyle = Normal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
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
                icon = { Icon(asset = vectorResource(id = R.drawable.ic_movie)) },
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
fun GenresContentPreview() {
    GenresContent(genres = listOf(Genre(1, "Action")))
}