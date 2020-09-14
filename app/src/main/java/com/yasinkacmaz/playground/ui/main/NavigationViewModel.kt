package com.yasinkacmaz.playground.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.yasinkacmaz.playground.data.Genre

class NavigationViewModel @ViewModelInject constructor() : ViewModel() {
    var currentGenre = mutableStateOf<Genre?>(null)

    fun onMovieCatalogSelected(genre: Genre) {
        currentGenre.value = genre
    }
}
