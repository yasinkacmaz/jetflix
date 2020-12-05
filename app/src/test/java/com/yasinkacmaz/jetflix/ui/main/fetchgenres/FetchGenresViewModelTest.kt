package com.yasinkacmaz.jetflix.ui.main.fetchgenres

import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.data.GenresResponse
import com.yasinkacmaz.jetflix.service.MovieService
import com.yasinkacmaz.jetflix.ui.main.genres.GenreUiModel
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class FetchGenresViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var movieService: MovieService

    @MockK
    private lateinit var genreUiModelMapper: GenreUiModelMapper

    private lateinit var fetchGenresViewModel: FetchGenresViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchGenresViewModel = spyk(FetchGenresViewModel(movieService, genreUiModelMapper))
    }

    @Test
    fun `fetchGenres success`() = coroutineTestRule.runBlockingTest {
        val genre = Genre(1, "Name")
        coEvery { movieService.fetchGenres() } returns GenresResponse(listOf(genre))
        val genreUiModel = GenreUiModel(genre = genre)
        every { genreUiModelMapper.map(genre) } returns genreUiModel

        fetchGenresViewModel.fetchGenres()

        coVerify { movieService.fetchGenres() }
        verify { genreUiModelMapper.map(genre) }
        verifyOrder {
            fetchGenresViewModel.uiValue = FetchGenresViewModel.FetchGenresUiState(loading = true)
            fetchGenresViewModel.uiValue = FetchGenresViewModel.FetchGenresUiState(
                loading = false,
                genreUiModels = listOf(genreUiModel)
            )
        }
    }

    @Test
    fun `fetchGenres error`() = coroutineTestRule.runBlockingTest {
        val exception = IOException()
        coEvery { movieService.fetchGenres() } throws exception

        fetchGenresViewModel.fetchGenres()

        coVerify { movieService.fetchGenres() }
        verifyOrder {
            fetchGenresViewModel.uiValue = FetchGenresViewModel.FetchGenresUiState(loading = true)
            fetchGenresViewModel.uiValue = FetchGenresViewModel.FetchGenresUiState(loading = false, error = exception)
        }
    }
}
