package com.yasinkacmaz.jetflix.ui.moviedetail

import com.yasinkacmaz.jetflix.ui.favorites.FavoritesDataStore
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.CreditsMapper
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImageMapper
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.ViewModelTest
import com.yasinkacmaz.jetflix.util.client.FakeMovieClient
import com.yasinkacmaz.jetflix.util.json
import com.yasinkacmaz.jetflix.util.test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test

class MovieDetailViewModelTest: ViewModelTest() {

    private val movieService = FakeMovieClient()

    private val movieId = 1337

    private val movieDetailMapper = MovieDetailMapper()
    private val creditsMapper = CreditsMapper()
    private val imageMapper = ImageMapper()

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @Test
    fun `fetchMovieDetail success`() = runTest {
        movieDetailViewModel = createViewModel()

        val stateValues = movieDetailViewModel.uiState.test()

        val expectedState = MovieDetailViewModel.MovieDetailUiState(
            movieDetailMapper.map(movieService.movieDetailResponse),
            creditsMapper.map(movieService.creditsResponse),
            imageMapper.map(movieService.imagesResponse),
            isFavorite = false,
            loading = false,
        )
        stateValues.last() shouldBe expectedState
    }

    @Test
    fun `fetchMovieDetail error`() = runTest {
        movieService.movieDetailException = IOException()

        movieDetailViewModel = createViewModel()
        val stateValues = movieDetailViewModel.uiState.test()

        stateValues.last().error.shouldBeInstanceOf<IOException>()
    }

    @Test
    fun `Should add to favorites when onFavoriteClicked`() = runTest {
        movieDetailViewModel = createViewModel()
        val stateValues = movieDetailViewModel.uiState.test()

        movieDetailViewModel.onFavoriteClicked()

        stateValues.last().isFavorite shouldBe true
    }

    @Test
    fun `Should remove favorite movie from favorites when onFavoriteClicked`() = runTest {
        movieDetailViewModel = createViewModel()
        val stateValues = movieDetailViewModel.uiState.test()

        movieDetailViewModel.onFavoriteClicked()
        movieDetailViewModel.onFavoriteClicked()

        stateValues.get(stateValues.lastIndex - 1).isFavorite shouldBe true
        stateValues.last().isFavorite shouldBe false
    }

    private fun createViewModel() = MovieDetailViewModel(
        movieId,
        movieService,
        movieDetailMapper,
        creditsMapper,
        imageMapper,
        FavoritesDataStore(json, FakeStringDataStore()),
    )
}
