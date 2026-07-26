package com.yasinkacmaz.jetflix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.yasinkacmaz.jetflix.ui.favorites.FavoritesScreen
import com.yasinkacmaz.jetflix.ui.main.MainScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailViewModel
import com.yasinkacmaz.jetflix.ui.moviedetail.image.ImagesScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.person.PeopleGridScreen
import com.yasinkacmaz.jetflix.ui.profile.ProfileScreen
import com.yasinkacmaz.jetflix.ui.settings.SettingsScreen
import com.yasinkacmaz.jetflix.ui.splash.SplashScreen
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.title_cast
import jetflix.composeapp.generated.resources.title_crew
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
private fun movieDetailViewModel(movieId: Int): MovieDetailViewModel =
    koinViewModel(key = movieId.toString()) { parametersOf(movieId) }

@Composable
fun SetupNavDisplay(backStack: MutableList<Screen>, onBack: () -> Unit) {
    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        sceneStrategies = listOf(SinglePaneSceneStrategy()),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                is Screen.Splash -> NavEntry(key) { SplashScreen() }

                is Screen.Movies -> NavEntry(key) {
                    MainScreen()
                }

                is Screen.MovieDetail -> NavEntry(key) {
                    MovieDetailScreen(movieDetailViewModel(key.movieId))
                }

                is Screen.MovieImages -> NavEntry(key) {
                    val uiState by movieDetailViewModel(key.movieId).uiState.collectAsState()
                    ImagesScreen(uiState.images, key.initialPage)
                }

                is Screen.MovieCast -> NavEntry(key) {
                    val uiState by movieDetailViewModel(key.movieId).uiState.collectAsState()
                    PeopleGridScreen(
                        stringResource(Res.string.title_cast, uiState.movieDetail?.title.orEmpty()),
                        uiState.credits.cast,
                    )
                }

                is Screen.MovieCrew -> NavEntry(key) {
                    val uiState by movieDetailViewModel(key.movieId).uiState.collectAsState()
                    PeopleGridScreen(
                        stringResource(Res.string.title_crew, uiState.movieDetail?.title.orEmpty()),
                        uiState.credits.crew,
                    )
                }

                is Screen.Profile -> NavEntry(key) {
                    ProfileScreen(koinViewModel { parametersOf(key.personId) })
                }

                is Screen.Favorites -> NavEntry(key) {
                    FavoritesScreen(favoritesViewModel = koinViewModel())
                }

                is Screen.Settings -> NavEntry(key) {
                    SettingsScreen()
                }
            }
        },
    )
}
