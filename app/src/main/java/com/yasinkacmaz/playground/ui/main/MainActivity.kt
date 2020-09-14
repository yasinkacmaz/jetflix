package com.yasinkacmaz.playground.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import com.yasinkacmaz.playground.R
import com.yasinkacmaz.playground.ui.main.common.ErrorContent
import com.yasinkacmaz.playground.ui.main.common.Loading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private val genresViewModel: GenresViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderUi()
    }

    private fun renderUi() {
        setContent {
            val genresUiState = genresViewModel.uiState.collectAsState().value
            if (genresUiState.shouldFetchGenres()) {
                genresViewModel.fetchGenres()
            }
            when {
                genresUiState.loading -> {
                    val context = ContextAmbient.current
                    val title = context.getString(R.string.fetching_genres)
                    Loading(title)
                }
                genresUiState.error != null -> {
                    ErrorContent(genresUiState.error.message.orEmpty())
                }
                genresUiState.genres.isNotEmpty() -> {
                    MainContent(genresUiState.genres)
                }
            }
        }
    }



}
