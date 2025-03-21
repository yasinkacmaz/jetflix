package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.moviedetail.person.Person
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.animation.AnimationDuration
import com.yasinkacmaz.jetflix.util.animation.springAnimation
import com.yasinkacmaz.jetflix.util.dpToPx
import com.yasinkacmaz.jetflix.util.openInChromeCustomTab

val LocalVibrantColor = compositionLocalOf<Animatable<Color, AnimationVector4D>> { error("No vibrant color defined") }
val LocalMovieId = compositionLocalOf<Int> { error("No movieId defined") }

@Composable
fun MovieDetailScreen(movieDetailViewModel: MovieDetailViewModel) {
    val uiState = movieDetailViewModel.uiState.collectAsState().value

    when {
        uiState.loading -> {
            LoadingColumn(stringResource(id = R.string.fetching_movie_detail))
        }

        uiState.error != null -> {
            ErrorColumn(uiState.error.message.orEmpty())
        }

        uiState.movieDetail != null -> {
            val defaultTextColor = MaterialTheme.colorScheme.onSurface
            val vibrantColor = remember { Animatable(defaultTextColor) }
            CompositionLocalProvider(
                LocalVibrantColor provides vibrantColor,
                LocalMovieId provides uiState.movieDetail.id,
            ) {
                Surface {
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

@Composable
fun MovieDetail(
    movieDetail: MovieDetail,
    cast: List<Person>,
    crew: List<Person>,
    images: List<Image>,
    isFavorite: Boolean,
    onFavoriteClicked: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        GetVibrantColorFromPoster(movieDetail.posterUrl, LocalVibrantColor.current)
        Box {
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
            IconButton(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(MaterialTheme.spacing.l)
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .align(Alignment.TopEnd)
                    .zIndex(2f),
                onClick = onFavoriteClicked,
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) {
                        stringResource(R.string.unfavorite_content_description)
                    } else {
                        stringResource(R.string.favorite_content_description)
                    },
                    tint = if (isFavorite) LocalVibrantColor.current.value else MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Title(
            title = movieDetail.title,
            originalTitle = movieDetail.originalTitle,
            homepage = movieDetail.homepage,
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
            color = LocalVibrantColor.current.value,
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
            headerResId = R.string.cast,
            onSeeAllClicked = { navController.navigate(Screen.MovieCast(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
        )

        MovieSection(
            items = crew,
            headerResId = R.string.crew,
            onSeeAllClicked = { navController.navigate(Screen.MovieCrew(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
        )

        MovieSection(
            items = images,
            headerResId = R.string.images,
            onSeeAllClicked = { navController.navigate(Screen.MovieImages(movieDetail.id, 0)) },
            itemContent = { item, index -> MovieImage(item, index) },
        )

        MovieSection(
            items = movieDetail.productionCompanies,
            headerResId = R.string.production_companies,
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
        colors = CardDefaults.cardColors(containerColor = LocalVibrantColor.current.value),
        modifier = modifier.height(360.dp),
    ) {
        JetflixImage(
            data = backdropUrl,
            contentScale = ContentScale.FillHeight,
            contentDescription = stringResource(R.string.backdrop_content_description, movieName),
            modifier = Modifier.fillMaxSize(),
            crossfade = AnimationDuration.LONG,
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
            contentDescription = stringResource(R.string.poster_content_description, movieName),
        )
    }
}

@Composable
private fun Title(title: String, originalTitle: String, homepage: String?) {
    val context = LocalContext.current
    val vibrantColor = LocalVibrantColor.current.value
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.l)
            .padding(top = MaterialTheme.spacing.s)
            .clickable(enabled = !homepage.isNullOrBlank()) {
                if (!homepage.isNullOrBlank()) {
                    homepage.openInChromeCustomTab(context, vibrantColor)
                }
            },
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
                    .border(1.25.dp, LocalVibrantColor.current.value, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.s, vertical = MaterialTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
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
            Icon(imageVector = asset, contentDescription = null, tint = LocalVibrantColor.current.value)
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetail, modifier: Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.l)) {
        MovieField(stringResource(R.string.release_date), movieDetail.releaseDate)
        MovieField(
            stringResource(R.string.duration),
            stringResource(R.string.duration_minutes, movieDetail.duration.toString()),
        )
        MovieField(stringResource(R.string.vote_average), movieDetail.voteAverage.toString())
        MovieField(stringResource(R.string.votes), movieDetail.voteCount.toString())
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
    @StringRes headerResId: Int,
    onSeeAllClicked: (() -> Unit)? = null,
    itemContent: @Composable (T, Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.spacing.l),
    ) {
        SectionHeader(headerResId, items.size, onSeeAllClicked)
        LazyRow(
            modifier = Modifier
                .testTag(stringResource(headerResId))
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
private fun SectionHeader(@StringRes headerResId: Int, count: Int, onClick: (() -> Unit)? = null) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.l),
    ) {
        Text(
            text = stringResource(headerResId),
            color = LocalVibrantColor.current.value,
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
                    text = stringResource(R.string.see_all, count),
                    color = LocalVibrantColor.current.value,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = MaterialTheme.spacing.xs),
                )
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.see_all),
                    tint = LocalVibrantColor.current.value,
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
            contentDescription = stringResource(R.string.poster_content_description),
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
        colors = CardDefaults.cardColors(containerColor = LocalVibrantColor.current.value.copy(alpha = 0.7f)),
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
                placeholder = painterResource(id = R.drawable.ic_jetflix),
                contentDescription = stringResource(
                    id = R.string.production_company_logo_content_description,
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
    Layout(modifier = Modifier.zIndex(1f), content = content) { measurables, constraints ->
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
