package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.SpacedRow
import com.yasinkacmaz.jetflix.util.fetchDominantColorFromPoster
import com.yasinkacmaz.jetflix.util.navigationBarsHeight
import com.yasinkacmaz.jetflix.util.statusBarsPadding
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

val DominantColorAmbient = ambientOf<MutableState<Color>> { error("No dominant color") }

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MovieDetailContent(movieId: Int) {
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
            val navigator = NavigatorAmbient.current
            val primaryColor = SelectedGenreAmbient.current.value.primaryColor
            val dominantColor = remember(movieDetailUiState.movieDetail.id) { mutableStateOf(primaryColor) }
            Providers(DominantColorAmbient provides dominantColor) {
                Stack(Modifier.fillMaxSize()) {
                    BackIcon { navigator.goBack() }
                    MovieDetail(movieDetailUiState.movieDetail)
                }
            }
        }
    }
}

@Composable
private fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.zIndex(8f).statusBarsPadding()) {
        Icon(Icons.Filled.ArrowBack, tint = Color.White)
    }
}

@Composable
private fun MovieDetail(movieDetail: MovieDetail) {
    ConstraintLayout(Modifier.background(MaterialTheme.colors.surface).verticalScroll(rememberScrollState())) {
        val (backdrop, poster, title, originalTitle, genres, specs, rateStars, overview, productionCompanies, space) = createRefs()
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
            modifier = Modifier.constrainAs(title) {
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
            text = movieDetail.overview,
            style = MaterialTheme.typography.body2.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.padding(horizontal = 16.dp).constrainAs(overview) {
                top.linkTo(rateStars.bottom, 32.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        ProductionCompanies(movieDetail.productionCompanies, Modifier.constrainAs(productionCompanies) {
            top.linkTo(overview.bottom, 16.dp)
            linkTo(startGuideline, endGuideline)
        })

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
private fun ProductionCompanies(companies: List<ProductionCompany>, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.production_companies),
            color = DominantColorAmbient.current.value,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 16.dp)
        )
        LazyRowFor(
            items = companies,
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)
        ) { company ->
            ProductionCompany(company = company)
            Spacer(modifier = Modifier.width(16.dp))
        }
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
