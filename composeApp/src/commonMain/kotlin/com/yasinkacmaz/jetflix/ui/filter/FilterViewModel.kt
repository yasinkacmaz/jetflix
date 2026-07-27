package com.yasinkacmaz.jetflix.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilterViewModel(
    private val filterDataStore: FilterDataStore,
    private val languageDataStore: LanguageDataStore,
    private val movieService: MovieService,
    private val genreUiModelMapper: GenreUiModelMapper,
) : ViewModel() {

    private val _filterState: MutableStateFlow<FilterState?> = MutableStateFlow(null)
    val filterState: StateFlow<FilterState?> = _filterState.asStateFlow()

    init {
        listenFilterStateChanges()
    }

    private fun listenFilterStateChanges() = viewModelScope.launch {
        val genresFlow = languageDataStore.languageCode.map {
            runCatching { movieService.fetchGenres().genres.map(genreUiModelMapper::map) }.getOrDefault(emptyList())
        }

        combine(filterDataStore.filterState, genresFlow) { filterState, genres -> filterState.copy(genres = genres) }
            .collect { updatedFilterState -> _filterState.update { updatedFilterState } }
    }

    fun onFilterStateChanged(filterState: FilterState) {
        viewModelScope.launch {
            filterDataStore.onFilterStateChanged(filterState)
        }
    }
}
