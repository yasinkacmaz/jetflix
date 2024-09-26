package com.yasinkacmaz.jetflix.ui.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yasinkacmaz.jetflix.data.service.MovieService
import com.yasinkacmaz.jetflix.ui.filter.FilterDataStore
import com.yasinkacmaz.jetflix.ui.filter.MovieRequestOptionsMapper
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.ui.movies.movie.MovieMapper
import kotlinx.coroutines.flow.first

class MoviesPagingSource(
    private val movieService: MovieService,
    private val filterDataStore: FilterDataStore,
    private val movieMapper: MovieMapper,
    private val movieRequestOptionsMapper: MovieRequestOptionsMapper,
    private val searchQuery: String = "",
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val options = movieRequestOptionsMapper.map(filterDataStore.filterState.first())
            val moviesResponse = if (searchQuery.isNotBlank()) {
                movieService.search(page, searchQuery)
            } else {
                movieService.fetchMovies(page, options)
            }
            val movies = moviesResponse.movies.map(movieMapper::map)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= moviesResponse.totalPages) null else moviesResponse.page + 1,
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
