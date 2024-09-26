package com.yasinkacmaz.jetflix.ui.movies

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MoviesViewModel(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper,
    private val movieRequestOptionsMapper: MovieRequestOptionsMapper,
    filterDataStore: FilterDataStore,
    languageDataStore: LanguageDataStore,
) : ViewModel() {
    val moviesPagingData = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            MoviesPagingSource(
                movieService,
                filterDataStore,
                movieMapper,
                movieRequestOptionsMapper,
                searchQuery.value,
            )
        },
    ).flow.cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @VisibleForTesting
    val searchQueryChanges = MutableSharedFlow<String>()
    private val filterStateChanges = MutableSharedFlow<FilterState>()
    private val selectedLanguageChanges = MutableSharedFlow<Unit>()

    val stateChanges = listOf(
        filterStateChanges.asSharedFlow(),
        selectedLanguageChanges.asSharedFlow(),
        searchQueryChanges.asSharedFlow().debounce(SEARCH_DEBOUNCE_MS).distinctUntilChanged(),
    )

    init {
        filterDataStore.filterState
            .drop(1)
            .onEach { filterStateChanges.emit(it) }
            .launchIn(viewModelScope)

        languageDataStore.languageCode
            .drop(1)
            .onEach { selectedLanguageChanges.emit(Unit) }
            .launchIn(viewModelScope)

        _searchQuery
            .runningReduce { previousQuery, newQuery ->
                val isEmptySearch = previousQuery.isEmpty() && newQuery.isBlank()
                val isSearchTooShort = previousQuery.isBlank() && newQuery.length < SEARCH_MINIMUM_LENGTH
                if (!(isEmptySearch || isSearchTooShort)) {
                    searchQueryChanges.emit(newQuery)
                }
                newQuery
            }
            .launchIn(viewModelScope)
    }

    fun onSearch(query: String) {
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        private const val SEARCH_MINIMUM_LENGTH = 3
    }
}
