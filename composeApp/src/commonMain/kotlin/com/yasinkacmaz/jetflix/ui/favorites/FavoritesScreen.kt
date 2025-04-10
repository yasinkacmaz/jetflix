package com.yasinkacmaz.jetflix.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieItem
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.spacing
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.back
import jetflix.composeapp.generated.resources.favorites
import jetflix.composeapp.generated.resources.no_favorites_found
import org.jetbrains.compose.resources.stringResource

private const val COLUMN_COUNT = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(favoritesViewModel: FavoritesViewModel) {
    Scaffold(
        topBar = {
            val navController = LocalNavController.current
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(stringResource(Res.string.favorites)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
    ) { contentPadding ->
        val favorites by favoritesViewModel.favorites.collectAsState()
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(Res.string.no_favorites_found), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            val navController = LocalNavController.current
            val onMovieClicked: (Int) -> Unit = { movieId -> navController.navigate(Screen.MovieDetail(movieId)) }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = contentPadding.calculateTopPadding()),
                contentPadding = PaddingValues(
                    bottom = contentPadding.calculateBottomPadding(),
                    start = MaterialTheme.spacing.s,
                    end = MaterialTheme.spacing.s,
                ),
                columns = GridCells.Fixed(COLUMN_COUNT),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
                content = {
                    items(favorites) {
                        MovieItem(it, Modifier.height(280.dp), onMovieClicked)
                    }
                },
            )
        }
    }
}
