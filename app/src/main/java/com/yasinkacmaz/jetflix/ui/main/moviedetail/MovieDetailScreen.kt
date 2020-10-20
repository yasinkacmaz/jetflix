package com.yasinkacmaz.jetflix.ui.main.moviedetail

import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Public
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.zIndex
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.main.genres.SelectedGenreAmbient
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Credits
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.Gender
import com.yasinkacmaz.jetflix.ui.main.moviedetail.credits.toPlaceholderImageRes
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.SpacedRow
import com.yasinkacmaz.jetflix.util.CircleTopCropTransformation
import com.yasinkacmaz.jetflix.util.fetchDominantColorFromPoster
import com.yasinkacmaz.jetflix.util.navigationBarsHeight
import com.yasinkacmaz.jetflix.util.statusBarsPadding
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale

val DominantColorAmbient = ambientOf<MutableState<Color>> { error("No dominant color") }

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MovieDetailScreen(movieId: Int) {
    val movieDetailViewModel: MovieDetailViewModel = viewModel(key = movieId.toString())
    val movieDetailUiState = movieDetailViewModel.uiState.collectAsState().value
    onActive {
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
            val primaryColor = SelectedGenreAmbient.current.value.primaryColor
            val dominantColor = remember(movieDetailUiState.movieDetail.id) { mutableStateOf(primaryColor) }
            Providers(DominantColorAmbient provides dominantColor) {
                AppBar(movieDetailUiState.movieDetail.homepage)
                MovieDetail(movieDetailUiState.movieDetail, movieDetailUiState.credits, movieDetailUiState.images)
            }
        }
    }
}

@Composable
private fun AppBar(homepage: String?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 2.dp).zIndex(8f)
    ) {
        val navigator = NavigatorAmbient.current
        IconButton(onClick = { navigator.goBack() }) {
            Icon(Icons.Filled.ArrowBack, tint = Color.White)
        }
        if (homepage != null) {
            val dominantColor = DominantColorAmbient.current.value
            val context = ContextAmbient.current
            IconButton(onClick = {
                openHomepage(context, homepage, dominantColor)
            }) {
                Icon(Icons.Rounded.Public, tint = Color.White)
            }
        }
    }
}


private fun openHomepage(context: Context, homepage: String, dominantColor: Color) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.setToolbarColor(dominantColor.toArgb()).build()
    customTabsIntent.launchUrl(context, Uri.parse(homepage))
}

@Composable
private fun MovieDetail(movieDetail: MovieDetail, credits: Credits, images: List<Image>) {
    ConstraintLayout(Modifier.background(MaterialTheme.colors.surface).verticalScroll(rememberScrollState())) {
        val (backdrop, poster, title, originalTitle, genres, specs, rateStars, tagline, overview) = createRefs()
        val (cast, crew, imagesSection, productionCompanies, space) = createRefs()
        val startGuideline = createGuidelineFromStart(16.dp)
        val endGuideline = createGuidelineFromEnd(16.dp)

        fetchDominantColorFromPoster(movieDetail.posterUrl, DominantColorAmbient.current)
        Backdrop(backdropUrl = movieDetail.backdropUrl, Modifier.constrainAs(backdrop) {})
        Poster(movieDetail.posterUrl, Modifier.zIndex(17f).width(120.dp).constrainAs(poster) {
            top.linkTo(backdrop.top)
            linkTo(startGuideline, endGuideline)
        }.padding(top = 210.dp))

        Text(
            text = movieDetail.title.toUpperCase(Locale.getDefault()),
            style = MaterialTheme.typography.h2.copy(
                fontSize = 22.sp,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp).constrainAs(title) {
                top.linkTo(poster.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        Text(
            text = "(${movieDetail.originalTitle})",
            style = MaterialTheme.typography.subtitle2.copy(
                fontStyle = FontStyle.Italic,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.constrainAs(originalTitle) {
                top.linkTo(title.bottom)
                linkTo(startGuideline, endGuideline)
            }
        )

        Text(
            text = movieDetail.genres.take(4).map(Genre::name).joinToString(),
            style = MaterialTheme.typography.subtitle1.copy(letterSpacing = 2.sp),
            modifier = Modifier.constrainAs(genres) {
                top.linkTo(originalTitle.bottom, 12.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieFields(movieDetail, modifier = Modifier.constrainAs(specs) {
            top.linkTo(genres.bottom, 12.dp)
            linkTo(startGuideline, endGuideline)
        })

        RateStars(movieDetail.voteAverage, modifier = Modifier.constrainAs(rateStars) {
            top.linkTo(specs.bottom, 12.dp)
            linkTo(startGuideline, endGuideline)
        })

        Text(
            text = movieDetail.tagline,
            color = DominantColorAmbient.current.value,
            style = MaterialTheme.typography.body2.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp).constrainAs(tagline) {
                top.linkTo(rateStars.bottom, 32.dp)
            }
        )

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.body2.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.padding(horizontal = 16.dp).constrainAs(overview) {
                top.linkTo(tagline.bottom, 8.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            credits.cast,
            { MovieSectionHeader(titleResId = R.string.cast) },
            { Person(it.profilePhotoUrl, it.name, it.character, it.gender) },
            Modifier.constrainAs(cast) {
                top.linkTo(overview.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            credits.crew,
            { MovieSectionHeader(titleResId = R.string.crew) },
            { Person(it.profilePhotoUrl, it.name, it.job, it.gender) },
            Modifier.constrainAs(crew) {
                top.linkTo(cast.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            images,
            { ImagesSectionHeader() },
            { Image(it) },
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

        Spacer(modifier = Modifier.navigationBarsHeight().constrainAs(space) {
            top.linkTo(productionCompanies.bottom)
        })
    }
}

@Composable
private fun Backdrop(backdropUrl: String, modifier: Modifier) {
    val arcHeight = 240.dp.value * DensityAmbient.current.density
    Card(
        elevation = 16.dp,
        shape = BottomArcShape(arcHeight = arcHeight),
        modifier = modifier.fillMaxWidth().height(300.dp)
    ) {
        CoilImage(
            data = backdropUrl,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter(Color(0x14000000), BlendMode.SrcOver),
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Poster(posterUrl: String, modifier: Modifier) {
    Card(elevation = 16.dp, shape = RoundedCornerShape(8.dp), modifier = modifier) {
        CoilImage(data = posterUrl, contentScale = ContentScale.FillWidth)
    }
}

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
    val dominantColor = DominantColorAmbient.current.value
    Row(modifier.padding(start = 4.dp)) {
        val maxVote = 10
        val starCount = 5
        repeat(starCount) { starIndex ->
            val voteStarCount = voteAverage / (maxVote / starCount)
            val (tint, asset) = when {
                voteStarCount >= starIndex + 1 -> {
                    dominantColor to Icons.Filled.Star
                }
                voteStarCount in starIndex.toDouble()..(starIndex + 1).toDouble() -> {
                    dominantColor to Icons.Filled.StarHalf
                }
                else -> {
                    dominantColor to Icons.Filled.StarOutline
                }
            }
            Icon(asset = asset, tint = tint)
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun MovieFields(movieDetail: MovieDetail, modifier: Modifier) {
    SpacedRow(spaceBetween = 16.dp, modifier = modifier) {
        val context = ContextAmbient.current
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
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
        )
    }
}

@Composable
private fun <T : Any> MovieSection(
    items: List<T>,
    header: @Composable () -> Unit,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        header()
        LazyRowFor(
            items = items,
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp)
        ) { item ->
            itemContent(item)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun MovieSectionHeader(@StringRes titleResId: Int) = Text(
    text = stringResource(titleResId),
    color = DominantColorAmbient.current.value,
    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(start = 16.dp)
)

@Composable
private fun Person(profilePhotoUrl: String?, name: String, job: String, gender: Gender) {
    Column(Modifier.padding(4.dp).width(140.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CoilImage(
            data = profilePhotoUrl ?: gender.toPlaceholderImageRes(),
            modifier = Modifier.size(120.dp).drawShadow(8.dp, CircleShape, false),
            fadeIn = true,
            requestBuilder = { transformations(CircleTopCropTransformation()) }
        )
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = job,
            style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun ImagesSectionHeader() {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(R.string.images),
            color = DominantColorAmbient.current.value,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(R.string.see_all),
            color = DominantColorAmbient.current.value,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
private fun Image(image: Image) {
    Card(
        Modifier.width(200.dp).height(110.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        CoilImage(
            data = image.url,
            contentScale = ContentScale.FillWidth,
            error = { Icon(asset = Icons.Default.Movie, tint = Color.DarkGray) }
        )
    }
}

@Composable
private fun ProductionCompany(company: ProductionCompany) {
    Card(
        Modifier.width(160.dp).height(100.dp),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = DominantColorAmbient.current.value,
        elevation = 8.dp
    ) {
        Column(Modifier.padding(4.dp)) {
            CoilImage(
                data = company.logoUrl,
                modifier = Modifier.size(85.dp, 67.dp).align(Alignment.CenterHorizontally),
                error = { Icon(asset = Icons.Default.Movie, tint = Color.DarkGray) }
            )
            Text(
                text = company.name,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
            )
        }
    }
}
