package com.yasinkacmaz.playground.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import com.yasinkacmaz.playground.data.Movie
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
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
