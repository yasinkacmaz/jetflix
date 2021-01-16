package com.yasinkacmaz.jetflix.ui.main.movies

import androidx.paging.PagingSource
import com.yasinkacmaz.jetflix.service.MovieService

class MoviesPagingSource(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper,
    private val genreId: Int
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val moviesResponse = movieService.fetchMovies(genreId, page)
            val movies = moviesResponse.movies.map(movieMapper::map)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = moviesResponse.page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
