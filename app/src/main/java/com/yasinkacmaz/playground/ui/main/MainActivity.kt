package com.yasinkacmaz.playground.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.playground.data.Movie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        render(emptyList())
        mainViewModel.fetchMovies()
    }

    private fun observeViewModel() {
        mainViewModel.movies.observe(this, ::render)
        mainViewModel.showLoading.observeAs
    }

    private fun render(movies: List<Movie>) {
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        topAppBar()
                    },
                    bottomBar = {
                        bottomNavigation()
                    },
                    bodyContent = {
                        LazyColumnFor(items = movies.take(5), contentPadding = InnerPadding(all = 16.dp)) { movie ->
                            movieCard(movie)
                        }
                    })
            }
        }
    }
}
