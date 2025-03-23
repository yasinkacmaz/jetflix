package com.yasinkacmaz.jetflix.ui.movies

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MoviesViewModel(
    private val filterDataStore: FilterDataStore,
    private val languageDataStore: LanguageDataStore,
    private val moviesPagingSource: MoviesPagingSource,
) : ViewModel() {

    private val _pagingState = MutableStateFlow<PagingState>(PagingState.Initial)
    val pagingState: StateFlow<PagingState> = _pagingState.asStateFlow()

    // TODO: Replace with immutable collection
    private val _movies = MutableStateFlow<MutableList<Movie>>(mutableListOf())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false

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
        listenFilterStateChanges()
        listenSelectedLanguageChanges()
        listenSearchQueryChanges()
        loadInitialData()
    }

    private fun listenFilterStateChanges() = viewModelScope.launch {
        filterDataStore.filterState
            .drop(1)
            .collectLatest { filterStateChanges.emit(it) }
    }

    private fun listenSelectedLanguageChanges() = viewModelScope.launch {
        languageDataStore.languageCode
            .drop(1)
            .collectLatest { selectedLanguageChanges.emit(Unit) }
    }

    private fun listenSearchQueryChanges() {
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

    private fun loadInitialData() {
        _pagingState.value = PagingState.Loading
        currentPage = 1
        isLastPage = false
        fetchMovies(currentPage)
    }

    fun loadMore() {
        if (_pagingState.value is PagingState.Loading || isLastPage) return

        _pagingState.value = PagingState.Loading
        val nextPage = currentPage + 1
        fetchMovies(nextPage)
    }

    fun retry() {
        if (_movies.value.isEmpty()) {
            loadInitialData()
        } else {
            loadMore()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _pagingState.value = PagingState.Refreshing
            currentPage = 1
            isLastPage = false
            fetchMovies(currentPage)
        }
    }

    private fun fetchMovies(page: Int) {
        viewModelScope.launch {
            val loadResult = moviesPagingSource.load(page, _searchQuery.value)
            _pagingState.value = if (loadResult.error != null) {
                PagingState.Error(loadResult.error)
            } else {
                PagingState.Success
            }
            if (loadResult.movies.isNotEmpty()) {
                _movies.update { currentItems ->
                    if (page == 1) currentItems.clear()
                    currentItems.addAll(loadResult.movies)
                    currentItems
                }
                currentPage = page
                isLastPage = loadResult.isLastPage
            }
        }
    }

    sealed class PagingState {
        data object Initial : PagingState()
        data object Loading : PagingState()
        data object Success : PagingState()
        data object Refreshing : PagingState()
        data class Error(val error: Throwable) : PagingState()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        private const val SEARCH_MINIMUM_LENGTH = 3
    }
}
