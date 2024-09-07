package com.yasinkacmaz.jetflix.ui.filter

import androidx.lifecycle.ViewModel
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.genres.GenreUiModelMapper
import com.yasinkacmaz.jetflix.util.Dispatchers
import com.yasinkacmaz.jetflix.util.onIO
import com.yasinkacmaz.jetflix.util.onMain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterDataStore: FilterDataStore,
    private val movieService: MovieService,
    private val genreUiModelMapper: GenreUiModelMapper,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val _filterState: MutableStateFlow<FilterState?> = MutableStateFlow(null)
    val filterState: StateFlow<FilterState?> = _filterState.asStateFlow().also {
        listenFilterStateChanges()
    }

    private fun listenFilterStateChanges() = dispatchers.onMain {
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
        dispatchers.onIO {
            filterDataStore.resetFilterState()
        }
    }

    fun onFilterStateChanged(filterState: FilterState) {
        dispatchers.onIO {
            filterDataStore.onFilterStateChanged(filterState)
        }
    }
}
