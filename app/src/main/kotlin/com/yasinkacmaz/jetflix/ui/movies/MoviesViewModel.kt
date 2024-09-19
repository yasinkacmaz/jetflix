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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
                movieMapper,
                movieRequestOptionsMapper,
                filterState,
                searchQuery.value,
            )
        },
    ).flow.cachedIn(viewModelScope)
    private var filterState: FilterState? = null

    @VisibleForTesting
    val searchQuery = MutableStateFlow("")

    private val searchQueryChanges = MutableSharedFlow<Unit>()
    private val filterStateChanges = MutableSharedFlow<FilterState>()
    private val selectedLanguageChanges = MutableSharedFlow<Unit>()

    val stateChanges: Array<SharedFlow<*>> = arrayOf(
        filterStateChanges.asSharedFlow(),
        searchQueryChanges.asSharedFlow(),
        selectedLanguageChanges.asSharedFlow(),
    )

    init {
        filterDataStore.filterState
            .onEach { filterState -> this.filterState = filterState }
            .onEach { filterStateChanges.emit(it) }
            .launchIn(viewModelScope)

        languageDataStore.languageCode
            .drop(1)
            .onEach { selectedLanguageChanges.emit(Unit) }
            .launchIn(viewModelScope)

        searchQuery
            .drop(1)
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { searchQueryChanges.emit(Unit) }
            .launchIn(viewModelScope)
    }

    fun onSearch(query: String) {
        if (searchQuery.value.isEmpty() && query.isBlank()) return
        if (searchQuery.value.isBlank() && query.length < SEARCH_MINIMUM_LENGTH) return

        searchQuery.tryEmit(query)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        private const val SEARCH_MINIMUM_LENGTH = 3
    }
}
