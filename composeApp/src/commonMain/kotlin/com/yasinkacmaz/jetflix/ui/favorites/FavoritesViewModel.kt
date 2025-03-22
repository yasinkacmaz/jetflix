package com.yasinkacmaz.jetflix.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(favoritesDataStore: FavoritesDataStore) : ViewModel() {
    val favorites = favoritesDataStore.favorites
        .map { it.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )
}
