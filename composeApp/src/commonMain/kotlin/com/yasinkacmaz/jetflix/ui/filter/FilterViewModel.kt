package com.yasinkacmaz.jetflix.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
        combine(
            filterDataStore.filterState,
            languageDataStore.languageCode,
        ) { filterState, _ ->
            filterState
        }.collectLatest { filterState ->
            val genres = try {
                movieService.fetchGenres().genres.map(genreUiModelMapper::map)
            } catch (_: Exception) {
                emptyList()
            }
            _filterState.update { filterState.copy(genres = genres) }
        }
    }

    fun onFilterStateChanged(filterState: FilterState) {
        viewModelScope.launch {
            filterDataStore.onFilterStateChanged(filterState)
        }
    }
}
