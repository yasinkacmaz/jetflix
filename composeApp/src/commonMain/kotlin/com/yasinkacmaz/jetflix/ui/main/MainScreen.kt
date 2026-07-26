package com.yasinkacmaz.jetflix.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculateThreePaneScaffoldValue
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.yasinkacmaz.jetflix.LocalNavigator
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailScreen
import com.yasinkacmaz.jetflix.ui.moviedetail.MovieDetailViewModel
import com.yasinkacmaz.jetflix.ui.movies.MoviesScreen
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.movie
import jetflix.composeapp.generated.resources.no_movie_selected
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun MainScreen() {
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val scaffoldDirective = calculatePaneScaffoldDirective(adaptiveInfo)
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator<String>(
        scaffoldDirective = scaffoldDirective,
    )
    var currentMovieDetailRoute by rememberSaveable(stateSaver = MovieDetailRouteSaver) { mutableStateOf(null) }
    var isFullScreen by remember { mutableStateOf(false) }

    val currentDirective = if (isFullScreen) {
        scaffoldNavigator.scaffoldDirective.copy(maxHorizontalPartitions = 1)
    } else {
        scaffoldNavigator.scaffoldDirective
    }

    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current

    var scaffoldWidth by remember { mutableIntStateOf(0) }
    var trackedProportion by remember { mutableFloatStateOf(0.35f) }

    val expansionState = if (scaffoldNavigator.scaffoldDirective.maxHorizontalPartitions > 1) {
        rememberPaneExpansionState(
            anchors = (3..7).map { PaneExpansionAnchor.Proportion(it / 10f) },
            initialAnchoredIndex = 0,
            consumeDragDelta = { delta ->
                if (scaffoldWidth <= 0) return@rememberPaneExpansionState delta
                val proportionDelta = delta / scaffoldWidth
                val newProportion = (trackedProportion + proportionDelta).coerceIn(0.3f, 0.7f)
                val consumed = newProportion - trackedProportion
                trackedProportion = newProportion
                consumed * scaffoldWidth
            },
        )
    } else {
        null
    }

    val currentAnchor = expansionState?.currentAnchor
    LaunchedEffect(currentAnchor) {
        if (currentAnchor is PaneExpansionAnchor.Proportion) {
            trackedProportion = currentAnchor.proportion
        }
    }

    val isMultiPane = currentDirective.maxHorizontalPartitions > 1
    LaunchedEffect(isMultiPane) {
        if (!isMultiPane && !isFullScreen) {
            val isDisplayingDetail =
                scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
            val hasNoSelection = currentMovieDetailRoute == null
            if (isDisplayingDetail && hasNoSelection) {
                scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.List)
            }
        }
    }

    val navigationState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)
    NavigationBackHandler(
        state = navigationState,
        isBackEnabled = scaffoldNavigator.canNavigateBack(),
        onBackCompleted = {
            scope.launch {
                scaffoldNavigator.navigateBack()
            }
        },
    )

    ListDetailPaneScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .onSizeChanged { scaffoldWidth = it.width },
        directive = currentDirective,
        value = if (isFullScreen) {
            calculateThreePaneScaffoldValue(
                maxHorizontalPartitions = 1,
                adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies(),
                currentDestination = ThreePaneScaffoldDestinationItem(
                    pane = ThreePaneScaffoldRole.Primary,
                    contentKey = scaffoldNavigator.currentDestination?.contentKey,
                ),
            )
        } else {
            scaffoldNavigator.scaffoldValue
        },
        paneExpansionDragHandle = if (expansionState != null) {
            { state ->
                val interactionSource = remember { MutableInteractionSource() }
                VerticalDragHandle(
                    modifier = Modifier.paneExpansionDraggable(
                        state = state,
                        minTouchTargetSize = 24.dp,
                        interactionSource = interactionSource,
                    ),
                    interactionSource = interactionSource,
                )
            }
        } else {
            null
        },
        paneExpansionState = expansionState,
        listPane = {
            AnimatedPane {
                MoviesScreen(
                    onMovieSelect = { movieId ->
                        if (isMultiPane) {
                            currentMovieDetailRoute = Screen.MovieDetail(movieId)
                            scope.launch {
                                scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, movieId.toString())
                            }
                        } else {
                            navigator.navigate(Screen.MovieDetail(movieId))
                        }
                    },
                )
            }
        },
        detailPane = {
            val route = currentMovieDetailRoute
            if (route != null) {
                AnimatedPane {
                    key(route.movieId) {
                        val viewModel = koinViewModel<MovieDetailViewModel>(key = route.movieId.toString()) {
                            parametersOf(route.movieId)
                        }
                        MovieDetailScreen(
                            movieDetailViewModel = viewModel,
                            isExpanded = scaffoldNavigator.scaffoldDirective.maxHorizontalPartitions > 1,
                            isFullScreen = isFullScreen,
                            onBack = {
                                isFullScreen = false
                                currentMovieDetailRoute = null
                                scope.launch { scaffoldNavigator.navigateBack() }
                            },
                            onToggleFullScreen = { isFullScreen = !isFullScreen },
                        )
                    }
                }
            } else if (currentDirective.maxHorizontalPartitions > 1) {
                AnimatedPane {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(Res.drawable.movie),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(Res.string.no_movie_selected),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
    )
}

private val MovieDetailRouteSaver = Saver<Screen.MovieDetail?, String>(
    save = { it?.let { route -> Json.encodeToString(Screen.MovieDetail.serializer(), route) } },
    restore = { it.let { json -> Json.decodeFromString(Screen.MovieDetail.serializer(), json) } },
)
