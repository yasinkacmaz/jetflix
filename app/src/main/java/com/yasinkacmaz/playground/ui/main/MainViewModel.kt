package com.yasinkacmaz.playground.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.service.MovieService
import com.yasinkacmaz.playground.data.Movie
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val movieService: MovieService) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()

    fun fetchMovies() {
        viewModelScope.launch {
            movies.value = movieService.fetchPopularMovies(1).movies
        }
    }
}
