package com.yasinkacmaz.jetflix.ui.movies

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.FilterState
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper,
    private val movieRequestOptionsMapper: MovieRequestOptionsMapper,
    filterDataStore: FilterDataStore
) : ViewModel() {
    private val pager: Pager<Int, Movie> =
        Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = ::initPagingSource)
    val movies: Flow<PagingData<Movie>> = pager.flow
    val filterStateChanges = MutableSharedFlow<FilterState>()
    private var filterState: FilterState? = null

    private val searchQuery = MutableStateFlow("")
    private val _searchQueryChanges = MutableSharedFlow<Unit>()
    val searchQueryChanges: SharedFlow<Unit> = _searchQueryChanges.asSharedFlow()

    @VisibleForTesting
    lateinit var pagingSource: MoviesPagingSource
        private set

    init {
        filterDataStore.filterState
            .onEach { filterState ->
                this.filterState = filterState
                filterStateChanges.emit(filterState)
            }
            .launchIn(viewModelScope)

        searchQuery
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { _searchQueryChanges.emit(Unit) }
            .launchIn(viewModelScope)
    }

    @VisibleForTesting
    fun initPagingSource() = MoviesPagingSource(
        movieService,
        movieMapper,
        movieRequestOptionsMapper,
        filterState,
        searchQuery.value
    ).also(::pagingSource::set)

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
