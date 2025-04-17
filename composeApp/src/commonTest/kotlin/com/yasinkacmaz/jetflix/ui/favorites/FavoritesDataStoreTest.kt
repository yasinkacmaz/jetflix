package com.yasinkacmaz.jetflix.ui.favorites

import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.json
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class FavoritesDataStoreTest {

    private val fakeStringDataStore = FakeStringDataStore()
    private val favoritesDataStore = FavoritesDataStore(json, fakeStringDataStore)

    private val movie = Movie(
        id = 1,
        name = "Inception",
        posterPath = "poster",
        releaseDate = "release",
        voteAverage = 8.8,
        voteCount = 100,
    )

    @Test
    fun `Should return empty favorites when no data is present`() = runTest {
        fakeStringDataStore.set("")

        val favorites = favoritesDataStore.favorites.first()

        favorites shouldBe emptySet()
    }

    @Test
    fun `Should add a movie to favorites`() = runTest {
        favoritesDataStore.addToFavorites(movie)

        val favorites = favoritesDataStore.favorites.first()

        favorites.size shouldBe 1
        favorites.contains(movie) shouldBe true
    }

    @Test
    fun `Should not add duplicate movies to favorites`() = runTest {
        favoritesDataStore.addToFavorites(movie)
        favoritesDataStore.addToFavorites(movie)

        val favorites = favoritesDataStore.favorites.first()

        favorites.size shouldBe 1
    }

    @Test
    fun `Should remove a movie from favorites`() = runTest {
        favoritesDataStore.addToFavorites(movie)
        favoritesDataStore.removeFromFavorites(movie.id)

        val favorites = favoritesDataStore.favorites.first()

        favorites shouldBe emptySet()
    }

    @Test
    fun `Should return true if movie is in favorites`() = runTest {
        favoritesDataStore.addToFavorites(movie)

        val isFavorite = favoritesDataStore.isFavorite(movie.id)

        isFavorite shouldBe true
    }

    @Test
    fun `Should return false if movie is not in favorites`() = runTest {
        favoritesDataStore.addToFavorites(movie)
        favoritesDataStore.removeFromFavorites(movie.id)

        val isFavorite = favoritesDataStore.isFavorite(movie.id)

        isFavorite shouldBe false
    }
}
