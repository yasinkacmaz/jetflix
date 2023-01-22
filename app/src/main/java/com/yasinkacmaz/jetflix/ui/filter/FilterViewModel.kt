package com.yasinkacmaz.jetflix.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterDataStore: FilterDataStore,
    private val movieService: MovieService,
    private val genreUiModelMapper: GenreUiModelMapper
) : ViewModel() {

    private val _filterState: MutableStateFlow<FilterState?> = MutableStateFlow(null)
    val filterState: StateFlow<FilterState?> = _filterState.asStateFlow().also {
        listenFilterStateChanges()
    }

    private fun listenFilterStateChanges() = viewModelScope.launch {
        val genres = try {
            movieService.fetchGenres().genres.map(genreUiModelMapper::map)
        } catch (exception: Exception) {
            emptyList()
        }

        filterDataStore.filterState
            .map { filterState -> filterState.copy(genres = genres) }
            .collect(_filterState::value::set)
    }

    fun onResetClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            filterDataStore.resetFilterState()
        }
    }

    fun onFilterStateChanged(filterState: FilterState) {
        viewModelScope.launch(Dispatchers.IO) {
            filterDataStore.onFilterStateChanged(filterState)
        }
    }
}
