package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yasinkacmaz.jetflix.service.MovieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel @ViewModelInject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) : ViewModel() {

    lateinit var pagingSource: MoviesPagingSource

    val movies: Flow<PagingData<Movie>> = Pager(PagingConfig(pageSize = 20)) { pagingSource }.flow

    fun createPagingSource(genreId: Int) {
        pagingSource = MoviesPagingSource(movieService, movieMapper, genreId)
    }
}
