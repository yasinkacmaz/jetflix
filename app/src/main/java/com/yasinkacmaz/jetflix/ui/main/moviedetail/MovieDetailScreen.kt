package com.yasinkacmaz.jetflix.ui.main.moviedetail

import android.content.Context
import android.net.Uri
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animate
import androidx.compose.animation.animatedFloat
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
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
import androidx.compose.ui.drawLayer
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
import com.yasinkacmaz.jetflix.ui.main.moviedetail.image.Image
import com.yasinkacmaz.jetflix.ui.main.moviedetail.person.Person
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.navigation.Screen
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.SpacedRow
import com.yasinkacmaz.jetflix.util.animation.AnimationType
import com.yasinkacmaz.jetflix.util.animation.ScaleAnimation
import com.yasinkacmaz.jetflix.util.animation.springAnimation
import com.yasinkacmaz.jetflix.util.fetchDominantColorFromPoster
import com.yasinkacmaz.jetflix.util.navigationBarsHeightPlus
import com.yasinkacmaz.jetflix.util.statusBarsPadding
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
        modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 4.dp, vertical = 2.dp).zIndex(8f)
    ) {
        val navigator = NavigatorAmbient.current
        val tint = Color.White
        IconButton(onClick = { navigator.goBack() }) {
            Icon(Icons.Filled.ArrowBack, tint = tint)
        }
        if (homepage != null) {
            val dominantColor = DominantColorAmbient.current.value
            val context = ContextAmbient.current
            IconButton(onClick = { openHomepage(context, homepage, dominantColor) }) {
                Icon(Icons.Rounded.Public, tint = tint)
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
        Poster(movieDetail.posterUrl, Modifier.zIndex(17f).width(150.dp).constrainAs(poster) {
            top.linkTo(backdrop.top)
            linkTo(startGuideline, endGuideline)
        }.padding(top = 240.dp))

        Text(
            text = movieDetail.title,
            style = MaterialTheme.typography.h1.copy(
                fontSize = 26.sp,
                letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp).constrainAs(title) {
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
                modifier = Modifier.padding(horizontal = 16.dp).constrainAs(originalTitle) {
                    top.linkTo(title.bottom)
                    linkTo(startGuideline, endGuideline)
                }
            )
        } else {
            Spacer(modifier = Modifier.constrainAs(originalTitle) {
                top.linkTo(title.bottom)
                linkTo(startGuideline, endGuideline)
            })
        }

        GenreChips(movieDetail.genres.take(4), modifier = Modifier.Companion.constrainAs(genres) {
            top.linkTo(originalTitle.bottom, 16.dp)
            linkTo(startGuideline, endGuideline)
        })

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
            style = MaterialTheme.typography.body1.copy(
                letterSpacing = 2.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify
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

        val navigator = NavigatorAmbient.current
        MovieSection(
            credits.cast,
            { SectionHeaderWithDetail(R.string.cast) { navigator.navigateTo(Screen.PeopleGrid(credits.cast)) } },
            { Person(it.profilePhotoUrl, it.name, it.character, it.gender, Modifier.width(140.dp)) },
            Modifier.constrainAs(cast) {
                top.linkTo(overview.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            credits.crew,
            { SectionHeaderWithDetail(R.string.crew) { navigator.navigateTo(Screen.PeopleGrid(credits.crew)) } },
            { Person(it.profilePhotoUrl, it.name, it.character, it.gender, Modifier.width(140.dp)) },
            Modifier.constrainAs(crew) {
                top.linkTo(cast.bottom, 16.dp)
                linkTo(startGuideline, endGuideline)
            }
        )

        MovieSection(
            images,
            { SectionHeaderWithDetail(R.string.images) { navigator.navigateTo(Screen.Images(images = images)) } },
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

        Spacer(modifier = Modifier.navigationBarsHeightPlus(16.dp).constrainAs(space) {
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
        modifier = modifier.fillMaxWidth().height(360.dp)
    ) {
        CoilImage(
            data = backdropUrl,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter(Color(0x23000000), BlendMode.SrcOver),
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Poster(posterUrl: String, _modifier: Modifier) {
    // TODO: Remove excessive animations
    val type = AnimationType.ANIMATE
    var modifier = _modifier
    val animation = springAnimation

    val scale: Float = when (type) {
        AnimationType.ANIMATEDVALUE -> {
            // Animated Value, AnimatedFloatModel
            val animatedScale = animatedFloat(1f)
            onActive {
                animatedScale.animateTo(targetValue = 2f, anim = animation)
            }
            animatedScale.value
        }
        AnimationType.ANIMATE -> {
            // Animate, AnimatedFloatModel
            val isScaled = remember { mutableStateOf(false) }
            modifier = modifier.clickable(onClick = { isScaled.value = !isScaled.value })
            animate(target = if (isScaled.value) 2f else 1f, animSpec = animation)
        }
        AnimationType.TRANSITION -> {
            // Transition
            val scaleAnimation = remember { ScaleAnimation(toScale = 2f, animation = animation) }
            scaleAnimation.scale()
        }
    }

    Card(
        elevation = 24.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.drawLayer(scaleX = scale, scaleY = scale)
    ) {
        CoilImage(data = posterUrl, contentScale = ContentScale.FillWidth)
    }
}

@Composable
private fun GenreChips(genres: List<Genre>, modifier: Modifier) {
    SpacedRow(spaceBetween = 8.dp, modifier = modifier) {
        genres.map(Genre::name).forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.subtitle1.copy(letterSpacing = 2.sp),
                modifier = Modifier.border(
                    1.25.dp,
                    DominantColorAmbient.current.value,
                    RoundedCornerShape(50)
                ).padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
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
    SpacedRow(spaceBetween = 20.dp, modifier = modifier) {
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
        LazyRowFor(items = items, contentPadding = PaddingValues(16.dp)) { item ->
            itemContent(item)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun MovieSectionHeader(@StringRes titleResId: Int) = Text(
    text = stringResource(titleResId),
    color = DominantColorAmbient.current.value,
    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
    modifier = Modifier.padding(start = 16.dp)
)

@Composable
private fun SectionHeaderWithDetail(@StringRes textRes: Int, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(textRes),
            color = DominantColorAmbient.current.value,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onClick).padding(4.dp)
        ) {
            Text(
                text = stringResource(R.string.see_all),
                color = DominantColorAmbient.current.value,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(Icons.Filled.ArrowForward, tint = DominantColorAmbient.current.value)
        }
    }
}

@Composable
private fun Image(image: Image) {
    Card(
        Modifier.width(240.dp).height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        CoilImage(
            data = image.url,
            contentScale = ContentScale.Crop,
            error = { Icon(asset = Icons.Default.Movie, tint = Color.DarkGray) }
        )
    }
}

@Composable
private fun ProductionCompany(company: ProductionCompany) {
    Card(
        Modifier.width(160.dp).height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp
    ) {
        Column(Modifier.background(DominantColorAmbient.current.value.copy(alpha = 0.7f)).padding(4.dp)) {
            CoilImage(
                data = company.logoUrl,
                contentScale = ContentScale.Inside,
                modifier = Modifier.size(150.dp, 85.dp).align(Alignment.CenterHorizontally),
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
