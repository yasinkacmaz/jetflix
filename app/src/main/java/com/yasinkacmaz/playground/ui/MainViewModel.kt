package com.yasinkacmaz.playground.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.api.MovieService
import com.yasinkacmaz.playground.data.Movie
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val movieService: MovieService) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()

    fun fetchMovies() {
        viewModelScope.launch {
            movies.value = movieService.fetchPopularMovies(1).movies
        }
    }
}
