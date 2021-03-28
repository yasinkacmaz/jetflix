package com.yasinkacmaz.jetflix.ui.main.moviedetail

import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.navigationBarsHeight
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.main.moviedetail.person.Person
import com.yasinkacmaz.jetflix.ui.navigation.LocalNavigator
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.SpacedRow
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
import com.yasinkacmaz.jetflix.util.animation.springAnimation
import com.yasinkacmaz.jetflix.util.randomColor

val LocalVibrantColor = compositionLocalOf<Animatable<Color, AnimationVector4D>> { error("No vibrant color") }

@Composable
fun MovieDetailScreen(movieId: Int) {
    val movieDetailViewModel: MovieDetailViewModel = viewModel(key = movieId.toString())
    val movieDetailUiState = movieDetailViewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        if (movieDetailUiState.movieDetail == null) {
            movieDetailViewModel.fetchMovieDetail(movieId)
        }
    }
    when {
        movieDetailUiState.loading -> {
            val title = stringResource(id = R.string.fetching_movie_detail)
            LoadingColumn(title)
        }
        movieDetailUiState.error != null -> {
            ErrorColumn(movieDetailUiState.error.message.orEmpty())
        }
        movieDetailUiState.movieDetail != null -> {
            val animatableColor = remember(movieId) { Animatable(Color.randomColor()) }
            CompositionLocalProvider(LocalVibrantColor provides animatableColor) {
                MovieDetail(movieDetailUiState.movieDetail, movieDetailUiState.credits, movieDetailUiState.images)
            }
        }
    }
}

@Composable
private fun AppBar(modifier: Modifier, homepage: String?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        val navigator = LocalNavigator.current
        val vibrantColor = LocalVibrantColor.current.value
        val scaleModifier = Modifier.scale(1.1f)
        IconButton(onClick = { navigator.goBack() }) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_icon_content_description),
                tint = vibrantColor,
                modifier = scaleModifier
            )
        }
        if (homepage != null) {
            val context = LocalContext.current
            IconButton(onClick = { openHomepage(context, homepage, vibrantColor) }) {
                Icon(
                    Icons.Rounded.OpenInNew,
                    contentDescription = stringResource(id = R.string.open_website_content_description),
                    tint = vibrantColor,
                    modifier = scaleModifier
                )
            }
        }
    }
}

private fun openHomepage(context: Context, homepage: String, vibrantColor: Color) {
    val schemeParams = CustomTabColorSchemeParams.Builder().setToolbarColor(vibrantColor.toArgb()).build()
    val customTabsIntent = CustomTabsIntent.Builder().setDefaultColorSchemeParams(schemeParams).build()
    customTabsIntent.launchUrl(context, Uri.parse(homepage))
}

@Composable
fun MovieDetail(movieDetail: MovieDetail, credits: Credits, images: List<Image>) {
    ConstraintLayout(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(rememberScrollState())
    ) {
        val (appbar, backdrop, poster, title, originalTitle, genres, specs, rateStars, tagline, overview) = createRefs()
        val (cast, crew, imagesSection, productionCompanies, space) = createRefs()
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
                .offset(y = 24.dp)
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
                }
        )

        Text(
            text = movieDetail.title,
            style = MaterialTheme.typography.h1.copy(
                fontSize = 26.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(title) {
                    top.linkTo(poster.bottom, 8.dp)
                    linkTo(startGuideline, endGuideline)
                }
        )

        if (movieDetail.title != movieDetail.originalTitle) {
            Text(
                text = "(${movieDetail.originalTitle})",
                style = MaterialTheme.typography.subtitle2.copy(
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .constrainAs(originalTitle) {
                        top.linkTo(title.bottom)
                        linkTo(startGuideline, endGuideline)
                    }
            )
        } else {
            Spacer(
                modifier = Modifier.constrainAs(originalTitle) {
                    top.linkTo(title.bottom)
                    linkTo(startGuideline, endGuideline)
                }
            )
        }

        GenreChips(
            movieDetail.genres.take(4),
            modifier = Modifier.constrainAs(genres) {
                top.linkTo(originalTitle.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieFields(
            movieDetail,
            modifier = Modifier.constrainAs(specs) {
                top.linkTo(genres.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        RateStars(
            movieDetail.voteAverage,
            modifier = Modifier.constrainAs(rateStars) {
                top.linkTo(specs.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        Text(
            text = movieDetail.tagline,
            color = LocalVibrantColor.current.value,
            style = MaterialTheme.typography.body1.copy(
                letterSpacing = 2.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(tagline) {
                    top.linkTo(rateStars.bottom, 32.dp)
                }
        )

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.body2.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(overview) {
                    top.linkTo(tagline.bottom, 8.dp)
                    linkTo(startGuideline, endGuideline)
                }
        )

        val navigator = LocalNavigator.current
        MovieSection(
            credits.cast,
            { SectionHeaderWithDetail(R.string.cast) { navigator.navigateTo(Screen.PeopleGrid(credits.cast)) } },
            { Person(it, Modifier.width(140.dp)) },
            Modifier.constrainAs(cast) {
                top.linkTo(overview.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
            tag = "cast"
        )

        MovieSection(
            credits.crew,
            { SectionHeaderWithDetail(R.string.crew) { navigator.navigateTo(Screen.PeopleGrid(credits.crew)) } },
            { Person(it, Modifier.width(140.dp)) },
            Modifier.constrainAs(crew) {
                top.linkTo(cast.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            },
            tag = "crew"
        )

        MovieSection(
            images,
            { SectionHeaderWithDetail(R.string.images) { navigator.navigateTo(Screen.Images(images = images)) } },
            { MovieImage(it) },
            Modifier.constrainAs(imagesSection) {
                top.linkTo(crew.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            movieDetail.productionCompanies,
            { MovieSectionHeader(titleResId = R.string.production_companies) },
            { ProductionCompany(it) },
            Modifier.constrainAs(productionCompanies) {
                top.linkTo(imagesSection.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        Spacer(
            modifier = Modifier
                .navigationBarsHeight(16.dp)
                .constrainAs(space) { top.linkTo(productionCompanies.bottom) }
        )
    }
}

@Composable
private fun Backdrop(backdropUrl: String, movieName: String, modifier: Modifier) {
    val arcHeight = 240.dp.value * LocalDensity.current.density
    Card(
        elevation = 16.dp,
        shape = BottomArcShape(arcHeight = arcHeight),
        modifier = modifier
            .fillMaxWidth()
            .height(360.dp)
    ) {
        CoilImage(
            data = backdropUrl,
            contentDescription = stringResource(id = R.string.backdrop_content_description, movieName),
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter.tint(Color(0x23000000), BlendMode.SrcOver),
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Poster(posterUrl: String, movieName: String, modifier: Modifier) {
    val isScaled = remember { mutableStateOf(false) }
    val scale =
        animateFloatAsState(targetValue = if (isScaled.value) 2.2f else 1f, animationSpec = springAnimation).value

    Card(
        elevation = 24.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .scale(scale)
            .clickable(onClick = { isScaled.value = !isScaled.value })
    ) {
        CoilImage(
            data = posterUrl,
            contentDescription = stringResource(id = R.string.movie_poster_content_description, movieName),
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
private fun GenreChips(genres: List<Genre>, modifier: Modifier) {
    SpacedRow(spaceBetween = 8.dp, modifier = modifier) {
        genres.map(Genre::name).forEach {
            Text(
                text = it.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(letterSpacing = 2.sp),
                modifier = Modifier
                    .border(
                        1.25.dp,
                        LocalVibrantColor.current.value,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
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
                voteStarCount in starIndex.toDouble()..(starIndex + 1).toDouble() -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }
            Icon(imageVector = asset, contentDescription = null, tint = LocalVibrantColor.current.value)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetail, modifier: Modifier) {
    SpacedRow(spaceBetween = 20.dp, modifier = modifier) {
        val context = LocalContext.current
        MovieField(context.getString(R.string.release_date), movieDetail.releaseDate)
        MovieField(
            context.getString(R.string.duration),
            context.getString(R.string.duration_minutes, movieDetail.duration.toString())
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
            style = MaterialTheme.typography.subtitle2.copy(fontSize = 13.sp, letterSpacing = 1.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun <T : Any> MovieSection(
    items: List<T>,
    header: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier,
    tag: String = ""
) {
    Column(modifier = modifier.fillMaxWidth()) {
        header()
        LazyRow(
            modifier = Modifier.semantics { testTag = tag },
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                count = items.size,
                itemContent = { index ->
                    itemContent(items[index])
                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        }
    }
}

@Composable
private fun MovieSectionHeader(@StringRes titleResId: Int) = Text(
    text = stringResource(titleResId),
    color = LocalVibrantColor.current.value,
    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(start = 16.dp)
)

@Composable
private fun SectionHeaderWithDetail(@StringRes textRes: Int, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(textRes),
            color = LocalVibrantColor.current.value,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(4.dp)
        ) {
            Text(
                text = stringResource(R.string.see_all),
                color = LocalVibrantColor.current.value,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = stringResource(R.string.see_all),
                tint = LocalVibrantColor.current.value
            )
        }
    }
}

@Composable
private fun MovieImage(image: Image) {
    Card(
        Modifier
            .width(240.dp)
            .height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        CoilImage(
            data = image.url,
            contentDescription = stringResource(id = R.string.poster_content_description),
            contentScale = ContentScale.Crop,
            error = { Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.DarkGray) }
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
        elevation = 8.dp
    ) {
        Column(
            Modifier
                .background(LocalVibrantColor.current.value.copy(alpha = 0.7f))
                .padding(4.dp)
        ) {
            CoilImage(
                data = company.logoUrl,
                contentDescription = stringResource(
                    id = R.string.production_company_logo_content_description,
                    company.name
                ),
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(150.dp, 85.dp)
                    .align(Alignment.CenterHorizontally),
                error = { Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color.DarkGray) }
            )
            Text(
                text = company.name,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
        }
    }
}
