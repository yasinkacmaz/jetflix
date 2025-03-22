package com.yasinkacmaz.jetflix.ui.favorites

import com.yasinkacmaz.jetflix.data.local.LocalDataStore
import com.yasinkacmaz.jetflix.ui.movies.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class FavoritesDataStore(private val json: Json, private val localDataStore: LocalDataStore) {

    private val favoriteMutex = Mutex()

    val favorites: Flow<Set<Movie>> = localDataStore.get(KEY_FAVORITES)
        .map { favoritesString ->
            if (favoritesString.isNullOrBlank()) {
                emptySet()
            } else {
                json.decodeFromString<Set<Movie>>(favoritesString)
            }
        }
        .catch { emit(emptySet()) }

    suspend fun addToFavorites(movie: Movie) {
        favoriteMutex.withLock {
            val currentFavorites: Set<Movie> = favorites.firstOrNull() ?: emptySet()
            val updatedFavorites = currentFavorites + movie
            localDataStore.set(KEY_FAVORITES, json.encodeToString(updatedFavorites))
        }
    }

    suspend fun removeFromFavorites(movieId: Int) {
        favoriteMutex.withLock {
            val currentFavorites: Set<Movie> = favorites.firstOrNull() ?: emptySet()
            val updatedFavorites = currentFavorites.filter { it.id != movieId }.toSet()
            localDataStore.set(KEY_FAVORITES, json.encodeToString(updatedFavorites))
        }
    }

    suspend fun isFavorite(movieId: Int): Boolean = favorites.firstOrNull().orEmpty().any { it.id == movieId }

    companion object {
        private const val KEY_FAVORITES = "favorites"
    }
}
