package com.yasinkacmaz.jetflix.ui.moviedetail

import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
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
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Error
import coil.compose.AsyncImagePainter.State.Loading
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.LocalNavController
import com.yasinkacmaz.jetflix.ui.moviedetail.credits.Person
import com.yasinkacmaz.jetflix.ui.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.moviedetail.person.Person
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.theme.imageTint
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
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
            val title = stringResource(id = R.string.fetching_movie_detail)
            LoadingColumn(title)
        }

        uiState.error != null -> {
            ErrorColumn(uiState.error.message.orEmpty())
        }

        uiState.movieDetail != null -> {
            val defaultTextColor = MaterialTheme.colorScheme.onBackground
            val vibrantColor = remember { Animatable(defaultTextColor) }
            CompositionLocalProvider(
                LocalVibrantColor provides vibrantColor,
                LocalMovieId provides uiState.movieDetail.id,
            ) {
                MovieDetail(uiState.movieDetail, uiState.credits.cast, uiState.credits.crew, uiState.images)
            }
        }
    }
}

@Composable
private fun AppBar(modifier: Modifier, homepage: String?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        val navController = LocalNavController.current
        val vibrantColor = LocalVibrantColor.current.value
        val scaleModifier = Modifier.scale(1.1f)
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_icon_content_description),
                tint = vibrantColor,
                modifier = scaleModifier,
            )
        }
        if (!homepage.isNullOrBlank()) {
            val context = LocalContext.current
            IconButton(onClick = { homepage.openInChromeCustomTab(context, vibrantColor) }) {
                Icon(
                    Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = stringResource(id = R.string.open_website_content_description),
                    tint = vibrantColor,
                    modifier = scaleModifier,
                )
            }
        }
    }
}

@Composable
fun MovieDetail(movieDetail: MovieDetail, cast: List<Person>, crew: List<Person>, images: List<Image>) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        val (appbar, backdrop, poster, title, genres, specs, rateStars, tagline, overview) = createRefs()
        val (castSection, crewSection, imagesSection, productionCompanies, space) = createRefs()
        val startGuideline = createGuidelineFromStart(16.dp)
        val endGuideline = createGuidelineFromEnd(16.dp)

        GetVibrantColorFromPoster(movieDetail.posterUrl, LocalVibrantColor.current)
        Backdrop(backdropUrl = movieDetail.backdropUrl, movieDetail.title, Modifier.constrainAs(backdrop) {})
        val posterWidth = 160.dp
        AppBar(
            homepage = movieDetail.homepage,
            modifier = Modifier
                .requiredWidth(posterWidth * 2.2f)
                .constrainAs(appbar) { centerTo(poster) }
                .offset(y = 24.dp),
        )
        Poster(
            movieDetail.posterUrl,
            movieDetail.title,
            Modifier
                .zIndex(17f)
                .width(posterWidth)
                .height(240.dp)
                .constrainAs(poster) {
                    centerAround(backdrop.bottom)
                    linkTo(startGuideline, endGuideline)
                },
        )
        Title(
            title = movieDetail.title,
            originalTitle = movieDetail.originalTitle,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(poster.bottom, 8.dp)
                linkTo(startGuideline, endGuideline)
            },
        )
        GenreChips(
            movieDetail.genres.take(4),
            modifier = Modifier.constrainAs(genres) {
                top.linkTo(title.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        MovieFields(
            movieDetail,
            modifier = Modifier.constrainAs(specs) {
                top.linkTo(genres.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        RateStars(
            movieDetail.voteAverage,
            modifier = Modifier.constrainAs(rateStars) {
                top.linkTo(specs.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        Text(
            text = movieDetail.tagline,
            color = LocalVibrantColor.current.value,
            style = MaterialTheme.typography.bodyLarge.copy(
                letterSpacing = 2.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(tagline) {
                    top.linkTo(rateStars.bottom, 32.dp)
                },
        )

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.bodyMedium.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif,
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(overview) {
                    top.linkTo(tagline.bottom, 8.dp)
                    linkTo(startGuideline, endGuideline)
                },
        )

        val navController = LocalNavController.current
        MovieSection(
            items = cast,
            headerResId = R.string.cast,
            onSeeAllClicked = { navController.navigate(Screen.MovieCast(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
            modifier = Modifier.constrainAs(castSection) {
                top.linkTo(overview.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        MovieSection(
            items = crew,
            headerResId = R.string.crew,
            onSeeAllClicked = { navController.navigate(Screen.MovieCrew(movieDetail.id)) },
            itemContent = { item, _ -> Person(item, Modifier.width(140.dp)) },
            modifier = Modifier.constrainAs(crewSection) {
                top.linkTo(castSection.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        MovieSection(
            items = images,
            headerResId = R.string.images,
            onSeeAllClicked = { navController.navigate(Screen.MovieImages(movieDetail.id, 0)) },
            itemContent = { item, index -> MovieImage(item, index) },
            modifier = Modifier.constrainAs(imagesSection) {
                top.linkTo(crewSection.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        MovieSection(
            items = movieDetail.productionCompanies,
            headerResId = R.string.production_companies,
            onSeeAllClicked = null,
            itemContent = { item, _ -> ProductionCompany(item) },
            modifier = Modifier.constrainAs(productionCompanies) {
                top.linkTo(imagesSection.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
        )

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .constrainAs(space) { top.linkTo(productionCompanies.bottom) },
        )
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(data = backdropUrl).crossfade(1500).build(),
            contentScale = ContentScale.FillHeight,
            contentDescription = stringResource(R.string.backdrop_content_description, movieName),
            modifier = modifier.fillMaxWidth(),
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
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.scale(scale),
        onClick = { isScaled.value = !isScaled.value },
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = stringResource(id = R.string.movie_poster_content_description, movieName),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Composable
private fun Title(title: String, originalTitle: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 26.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            ),
        )
        if (originalTitle.isNotBlank() && title != originalTitle) {
            Text(
                text = "($originalTitle)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}

@Composable
private fun GenreChips(genres: List<Genre>, modifier: Modifier) {
    Row(
        modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        genres.map(Genre::name).forEachIndexed { index, name ->
            Text(
                text = name.orEmpty(),
                style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 2.sp),
                modifier = Modifier
                    .border(1.25.dp, LocalVibrantColor.current.value, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
            )

            if (index != genres.lastIndex) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
    Row(modifier.padding(start = 4.dp)) {
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
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetail, modifier: Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = modifier) {
        val context = LocalContext.current
        MovieField(context.getString(R.string.release_date), movieDetail.releaseDate)
        MovieField(
            context.getString(R.string.duration),
            context.getString(R.string.duration_minutes, movieDetail.duration.toString()),
        )
        MovieField(context.getString(R.string.vote_average), movieDetail.voteAverage.toString())
        MovieField(context.getString(R.string.votes), movieDetail.voteCount.toString())
    }
}

@Composable
private fun MovieField(name: String, value: String) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp, letterSpacing = 1.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp),
        )
    }
}

@Composable
private fun <T : Any> MovieSection(
    items: List<T>,
    @StringRes headerResId: Int,
    onSeeAllClicked: (() -> Unit)? = null,
    itemContent: @Composable (T, Int) -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionHeader(headerResId, items.size, onSeeAllClicked)
        LazyRow(
            modifier = Modifier.testTag(LocalContext.current.getString(headerResId)),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                count = items.size,
                itemContent = { index ->
                    itemContent(items[index], index)
                    Spacer(modifier = Modifier.width(16.dp))
                },
            )
        }
    }
}

@Composable
private fun SectionHeader(@StringRes headerResId: Int, count: Int, onClick: (() -> Unit)? = null) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(headerResId),
            color = LocalVibrantColor.current.value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
        if (onClick != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(onClick = { onClick() })
                    .padding(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.see_all, count),
                    color = LocalVibrantColor.current.value,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 4.dp),
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
            .height(160.dp)
            .clickable { navController.navigate(Screen.MovieImages(movieId, index)) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        val request = ImageRequest.Builder(LocalContext.current)
            .data(image.url)
            .crossfade(true)
            .build()
        val painter = rememberAsyncImagePainter(
            model = request,
            placeholder = rememberVectorPainter(Icons.Default.Image),
            error = rememberVectorPainter(Icons.Default.BrokenImage),
        )
        val (colorFilter, contentScale) = when (painter.state) {
            is Error, is Loading -> ColorFilter.tint(MaterialTheme.colorScheme.imageTint()) to ContentScale.Fit
            else -> null to ContentScale.Crop
        }
        Image(
            painter = painter,
            colorFilter = colorFilter,
            contentDescription = stringResource(id = R.string.poster_content_description),
            contentScale = contentScale,
        )
    }
}

@Composable
private fun ProductionCompany(company: ProductionCompany) {
    Card(
        Modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(LocalVibrantColor.current.value.copy(alpha = 0.7f))
                .padding(4.dp),
        ) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(company.logoUrl)
                .crossfade(true)
                .build()
            val painter = rememberAsyncImagePainter(
                model = request,
                placeholder = painterResource(id = R.drawable.ic_jetflix),
                error = rememberVectorPainter(Icons.Default.BrokenImage),
            )
            val colorFilter = when (painter.state) {
                is Error -> ColorFilter.tint(MaterialTheme.colorScheme.imageTint())
                else -> null
            }
            Image(
                painter = painter,
                colorFilter = colorFilter,
                contentDescription = stringResource(
                    id = R.string.production_company_logo_content_description,
                    company.name,
                ),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(150.dp, 85.dp),
            )
            Text(
                text = company.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
            )
        }
    }
}
