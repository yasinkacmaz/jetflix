package com.yasinkacmaz.playground.ui.main

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
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.data.Genre
import com.yasinkacmaz.playground.ui.main.movie.Movies
import com.yasinkacmaz.playground.ui.theme.PlayGroundTheme

@Composable
fun MainContent(genres: List<Genre>) {
    val isDarkTheme = remember { mutableStateOf(false) }
    val navigationViewModel: NavigationViewModel = viewModel()
    navigationViewModel.currentGenre.value = genres.first()
    PlayGroundTheme(isDarkTheme = isDarkTheme.value) {
        Scaffold(
            topBar = {
                TopAppBar(isDarkTheme.value) {
                    isDarkTheme.value = it
                }
            },
            bottomBar = {
                BottomNavigation(genres, navigationViewModel)
            },
            bodyContent = { innerPadding ->
                Crossfade(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    current = navigationViewModel.currentGenre.value
                ) { genre ->
                    Movies(genre!!)
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
fun BottomNavigation(genres: List<Genre>, navigationViewModel: NavigationViewModel) {
    BottomNavigation {
        genres.forEach { genre ->
            val selected = genre == navigationViewModel.currentGenre.value
            BottomNavigationItem(
                label = { Text(genre.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                icon = { Icon(asset = vectorResource(id = R.drawable.ic_movie)) },
                selected = selected,
                onSelect = {
                    navigationViewModel.onMovieCatalogSelected(genre)
                }
            )
        }
    }
}

@Preview
@Composable
fun MainContentPreview() {
    MainContent(genres = listOf(Genre(1, "Action")))
}