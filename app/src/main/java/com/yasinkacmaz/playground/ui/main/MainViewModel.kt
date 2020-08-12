package com.yasinkacmaz.playground.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.playground.service.MovieService
import com.yasinkacmaz.playground.data.Movie
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(private val movieService: MovieService) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    val showLoading = MutableLiveData<Boolean>()

    fun fetchMovies() {
        viewModelScope.launch {
            showLoading.value = true
            try {
            movies.value = movieService.fetchPopularMovies(1).movies
            } catch (exception: Exception) {
                // TODO show error dialog l8r
            }
            showLoading.value = false
        }
    }
}
