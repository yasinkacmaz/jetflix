package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.common.Error
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.moviedetail.person.Person
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.CircleIconButton
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.animation.springAnimation
import com.yasinkacmaz.jetflix.util.dpToPx
import com.yasinkacmaz.jetflix.util.openInBrowser
import com.yasinkacmaz.jetflix.util.rateColor
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.back
import jetflix.composeapp.generated.resources.backdrop_content_description
import jetflix.composeapp.generated.resources.cast
import jetflix.composeapp.generated.resources.crew
import jetflix.composeapp.generated.resources.duration
import jetflix.composeapp.generated.resources.duration_minutes
import jetflix.composeapp.generated.resources.favorite_content_description
import jetflix.composeapp.generated.resources.fetching_movie_detail
import jetflix.composeapp.generated.resources.ic_jetflix
import jetflix.composeapp.generated.resources.images
import jetflix.composeapp.generated.resources.open_website_content_description
import jetflix.composeapp.generated.resources.poster_content_description
import jetflix.composeapp.generated.resources.production_companies
import jetflix.composeapp.generated.resources.production_company_logo_content_description
import jetflix.composeapp.generated.resources.release_date
import jetflix.composeapp.generated.resources.see_all
import jetflix.composeapp.generated.resources.unfavorite_content_description
import jetflix.composeapp.generated.resources.vote_average
import jetflix.composeapp.generated.resources.votes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

val LocalMovieId = compositionLocalOf<Int> { error("No movieId defined") }

@Composable
fun MovieDetailScreen(movieDetailViewModel: MovieDetailViewModel) {
    Surface {
        val uiState = movieDetailViewModel.uiState.collectAsState().value

        when {
            uiState.loading -> {
                Loading(modifier = Modifier.fillMaxSize(), title = stringResource(Res.string.fetching_movie_detail))
            }

            uiState.error != null -> {
                Error(modifier = Modifier.fillMaxSize(), message = uiState.error.message.orEmpty())
            }

            uiState.movieDetail != null -> {
                CompositionLocalProvider(LocalMovieId provides uiState.movieDetail.id) {
                    MovieDetail(
                        movieDetail = uiState.movieDetail,
                        cast = uiState.credits.cast,
                        crew = uiState.credits.crew,
                        images = uiState.images,
                        isFavorite = uiState.isFavorite,
                        onFavoriteClicked = movieDetailViewModel::onFavoriteClicked,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetail(
    movieDetail: MovieDetail,
    cast: List<Person>,
    crew: List<Person>,
    images: List<Image>,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val topAppBarOffset = with(LocalDensity.current) { scrollState.value.toDp() }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = MaterialTheme.spacing.l),
    ) {
        Box(Modifier.zIndex(1f)) {
            BackdropAndPoster {
                Backdrop(
                    modifier = Modifier.fillMaxWidth(),
                    backdropUrl = movieDetail.backdropUrl,
                    movieName = movieDetail.title,
                )
                Poster(
                    movieDetail.posterUrl,
                    movieDetail.title,
                    Modifier
                        .width(160.dp)
                        .height(240.dp),
                )
            }
            val navController = LocalNavController.current
            TopAppBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = MaterialTheme.spacing.l)
                    .offset(y = topAppBarOffset),
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    CircleIconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },

                actions = {
                    if (!movieDetail.homepage.isNullOrBlank()) {
                        val uriHandler = LocalUriHandler.current
                        CircleIconButton(onClick = { movieDetail.homepage.openInBrowser(uriHandler) }) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = stringResource(Res.string.open_website_content_description),
                            )
                        }
                        Spacer(Modifier.width(MaterialTheme.spacing.l))
                    }

                    CircleIconButton(onClick = onFavoriteClicked) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) {
                                stringResource(Res.string.unfavorite_content_description)
                            } else {
                                stringResource(Res.string.favorite_content_description)
                            },
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        }

        Title(
            title = movieDetail.title,
            originalTitle = movieDetail.originalTitle,
        )
        GenreChips(
            movieDetail.genres,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.l)
                .align(Alignment.CenterHorizontally),
        )
        MovieFields(
            movieDetail,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.m)
                .align(Alignment.CenterHorizontally),
        )
        RateStars(
            movieDetail.voteAverage,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.m)
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = movieDetail.tagline,
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.l)
                .padding(top = MaterialTheme.spacing.xl),
        )
        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.bodyLarge.copy(letterSpacing = 1.5.sp),
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.s)
                .padding(horizontal = MaterialTheme.spacing.l),
        )

        val navController = LocalNavController.current
        MovieSection(
            items = cast,
            headerResource = Res.string.cast,
            onSeeAllClicked = { navController.navigate(Screen.MovieCast(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
        )

        MovieSection(
            items = crew,
            headerResource = Res.string.crew,
            onSeeAllClicked = { navController.navigate(Screen.MovieCrew(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
        )

        MovieSection(
            items = images,
            headerResource = Res.string.images,
            onSeeAllClicked = { navController.navigate(Screen.MovieImages(movieDetail.id, 0)) },
            itemContent = { item, index -> MovieImage(item, index) },
        )

        MovieSection(
            items = movieDetail.productionCompanies,
            headerResource = Res.string.production_companies,
            onSeeAllClicked = null,
            itemContent = { item, _ -> ProductionCompany(item) },
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun Backdrop(backdropUrl: String, movieName: String, modifier: Modifier) {
    Card(
        elevation = CardDefaults.cardElevation(16.dp),
        shape = BottomArcShape(arcHeight = 120.dpToPx()),
        modifier = modifier.height(360.dp),
    ) {
        JetflixImage(
            data = backdropUrl,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(Res.string.backdrop_content_description, movieName),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun Poster(posterUrl: String, movieName: String, modifier: Modifier) {
    val isScaled = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (isScaled.value) 2.2f else 1f,
        animationSpec = springAnimation,
        label = "scale",
    ).value

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
        modifier = modifier.scale(scale),
        onClick = { isScaled.value = !isScaled.value },
    ) {
        JetflixImage(
            Modifier.fillMaxSize(),
            data = posterUrl,
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(Res.string.poster_content_description, movieName),
        )
    }
}

@Composable
private fun Title(title: String, originalTitle: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.l)
            .padding(top = MaterialTheme.spacing.s),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        if (originalTitle.isNotBlank() && title != originalTitle) {
            Text(
                text = "($originalTitle)",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall.copy(fontStyle = FontStyle.Italic, letterSpacing = 2.sp),
            )
        }
    }
}

@Composable
private fun GenreChips(genres: List<String>, modifier: Modifier) {
    Row(
        modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = MaterialTheme.spacing.l),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.s),
    ) {
        genres.forEach { genre ->
            Text(
                text = genre,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .border(1.25.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.s, vertical = MaterialTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
    val rateColor = Color.rateColor(voteAverage)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
        val maxVote = 10
        val starCount = 5
        repeat(starCount) { starIndex ->
            val voteStarCount = voteAverage / (maxVote / starCount)
            val asset = when {
                voteStarCount >= starIndex + 1 -> Icons.Filled.Star
                voteStarCount in starIndex.toDouble()..(starIndex + 1).toDouble() -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            Icon(imageVector = asset, contentDescription = null, tint = rateColor)
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetail, modifier: Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.l)) {
        MovieField(stringResource(Res.string.release_date), movieDetail.releaseDate)
        MovieField(
            stringResource(Res.string.duration),
            stringResource(Res.string.duration_minutes, movieDetail.duration.toString()),
        )
        MovieField(stringResource(Res.string.vote_average), movieDetail.voteAverage.toString())
        MovieField(stringResource(Res.string.votes), movieDetail.voteCount.toString())
    }
}

@Composable
private fun MovieField(name: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, style = MaterialTheme.typography.titleSmall)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
        )
    }
}

@Composable
private fun <T : Any> MovieSection(
    modifier: Modifier = Modifier,
    items: List<T>,
    headerResource: StringResource,
    onSeeAllClicked: (() -> Unit)? = null,
    itemContent: @Composable (T, Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.spacing.l),
    ) {
        SectionHeader(headerResource, items.size, onSeeAllClicked)
        LazyRow(
            modifier = Modifier
                .testTag(stringResource(headerResource))
                .padding(top = MaterialTheme.spacing.m),
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.l),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.m),
        ) {
            items(
                count = items.size,
                itemContent = { index ->
                    itemContent(items[index], index)
                },
            )
        }
    }
}

@Composable
private fun SectionHeader(headerResource: StringResource, count: Int, onClick: (() -> Unit)? = null) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.l),
    ) {
        Text(
            text = stringResource(headerResource),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )
        if (onClick != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = { onClick() })
                    .padding(MaterialTheme.spacing.xs),
            ) {
                Text(
                    text = stringResource(Res.string.see_all, count),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = MaterialTheme.spacing.xs),
                )
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(Res.string.see_all),
                )
            }
        }
    }
}

@Composable
private fun MovieImage(image: Image, index: Int) {
    val navController = LocalNavController.current
    val movieId = LocalMovieId.current
    Card(
        Modifier
            .width(240.dp)
            .height(200.dp)
            .clickable { navController.navigate(Screen.MovieImages(movieId, index)) },
    ) {
        JetflixImage(
            modifier = Modifier.fillMaxSize(),
            data = image.url,
            placeholder = rememberVectorPainter(Icons.Default.Image),
            error = rememberVectorPainter(Icons.Default.BrokenImage),
            contentDescription = stringResource(Res.string.poster_content_description),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ProductionCompany(company: ProductionCompany) {
    Card(
        Modifier
            .width(240.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.xs),
        ) {
            JetflixImage(
                modifier = Modifier.size(200.dp, 120.dp),
                data = company.logoUrl,
                contentScale = ContentScale.Fit,
                placeholder = painterResource(Res.drawable.ic_jetflix),
                contentDescription = stringResource(
                    Res.string.production_company_logo_content_description,
                    company.name,
                ),
            )
            Text(
                text = company.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = MaterialTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun BackdropAndPoster(content: @Composable () -> Unit) {
    Layout(modifier = Modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        val backdrop = placeables[0]
        val poster = placeables[1]
        val halfPosterHeight = poster.height / 2
        val totalHeight = backdrop.height + halfPosterHeight

        layout(constraints.maxWidth, totalHeight) {
            val centerHorizontal = constraints.maxWidth / 2
            backdrop.place(x = 0, y = 0)
            poster.placeRelative(x = centerHorizontal - poster.width / 2, y = backdrop.height - halfPosterHeight)
        }
    }
}
