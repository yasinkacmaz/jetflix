package com.yasinkacmaz.playground.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import com.yasinkacmaz.playground.data.Movie
import com.yasinkacmaz.playground.service.MovieService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

   private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
        mainViewModel.fetchMovies()
    }

    private fun observeViewModel() {
        mainViewModel.movies.observe(this) {
            val movieNames = it.map(Movie::name).joinToString()
            buildUi(movieNames)
        }
    }

    private fun buildUi(movieNames: String) {
        setContent {
            Text(text = movieNames)
        }
    }
}
