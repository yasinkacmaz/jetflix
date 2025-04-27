package com.yasinkacmaz.jetflix.ui.moviedetail.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.theme.spacing
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleGridScreen(title: String, people: List<Person>) {
    val horizontalPadding = MaterialTheme.spacing.l
    val gridState = rememberLazyGridState()
    val navController = LocalNavController.current
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars.add(
            WindowInsets(
                left = horizontalPadding,
                right = horizontalPadding,
            ),
        ),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.l),
            horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
            state = gridState,
            content = {
                items(people) {
                    Person(person = it, modifier = Modifier.animateItem())
                }
            },
        )
    }
}
