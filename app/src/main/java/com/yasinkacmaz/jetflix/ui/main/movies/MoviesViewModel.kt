package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.main.filter.FilterState
import com.yasinkacmaz.jetflix.ui.main.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.main.movie.Movie
import com.yasinkacmaz.jetflix.ui.main.movie.MovieMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel @ViewModelInject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper,
    private val movieRequestOptionsMapper: MovieRequestOptionsMapper,
    filterDataStore: FilterDataStore
) : ViewModel() {
    private val pager: Pager<Int, Movie> = Pager(PagingConfig(pageSize = 20), pagingSourceFactory = ::initPagingSource)
    val movies: Flow<PagingData<Movie>> = pager.flow
    val filterStateChanges = MutableSharedFlow<FilterState>()
    private var filterState: FilterState? = null
    private var genreId: Int? = null

    @VisibleForTesting
    lateinit var pagingSource: MoviesPagingSource
        private set

    init {
        filterDataStore.filterState
            .onEach { filterState ->
                filterStateChanges.emit(filterState)
                this.filterState = filterState
            }
            .launchIn(viewModelScope)
    }

    @VisibleForTesting
    fun initPagingSource() = MoviesPagingSource(
        movieService,
        movieMapper,
        movieRequestOptionsMapper,
        filterState,
        genreId
    ).also(::pagingSource::set)
}
