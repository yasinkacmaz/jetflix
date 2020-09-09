package com.yasinkacmaz.playground.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.ui.theme.PlayGroundTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.fetchMovies()
        renderUi()
    }

    private fun renderUi() {
        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }
            PlayGroundTheme(isDarkTheme = isDarkTheme.value) {
                Scaffold(
                    topBar = {
                        TopAppBar(isDarkTheme.value) {
                            isDarkTheme.value = it
                        }
                    },
                    bottomBar = {
                        BottomNavigation()
                    },
                    bodyContent = {
                        BodyContent()
                    })
            }
        }
    }

    @Composable
    private fun BodyContent() {
        val uiState = mainViewModel.uiState.collectAsState().value
        if (uiState.loading) {
            Loading()
        } else {
            Movies(uiState.movies)
        }
    }

    @Composable
    private fun Loading() {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalGravity = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.fetching_movies))
            CircularProgressIndicator(modifier = Modifier.preferredSize(40.dp).padding(top = 16.dp))
        }
    }
}
