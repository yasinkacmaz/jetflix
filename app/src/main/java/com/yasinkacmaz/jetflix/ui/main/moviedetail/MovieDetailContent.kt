package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.zIndex
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.navigation.NavigatorAmbient
import com.yasinkacmaz.jetflix.ui.widget.BottomArcShape
import com.yasinkacmaz.jetflix.ui.widget.SpacedRow
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MovieDetailContent(movieId: Int) {
    val movieDetailViewModel: MovieDetailViewModel = viewModel(key = movieId.toString())
    val movieDetailUiState = movieDetailViewModel.uiState.collectAsState().value
    onActive {
        movieDetailViewModel.fetchMovieDetail(movieId)
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
            MovieDetail(movieDetailUiState.movieDetail)
        }
    }
}

@Composable
private fun MovieDetail(movieDetail: MovieDetail) {
    val navigator = NavigatorAmbient.current
    BackIcon { navigator.goBack() }
    ScrollableColumn(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Backdrop(movieDetail.backdropUrl)
        PosterAndInformation(movieDetail, Modifier.offset(y = (-80).dp))
    }
}

@Composable
private fun Backdrop(backdropUrl: String) {
    val arcHeight = 240.dp.value * DensityAmbient.current.density
    Card(elevation = 16.dp, shape = BottomArcShape(arcHeight = arcHeight)) {
        CoilImage(
            data = backdropUrl,
            contentScale = ContentScale.FillHeight,
            colorFilter = ColorFilter(Color(0x14000000), BlendMode.SrcOver),
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )
    }
}

@Composable
private fun BackIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.zIndex(4f)) {
        Icon(Icons.Filled.ArrowBack, tint = Color.White)
    }
}

@Composable
private fun PosterAndInformation(movieDetail: MovieDetail, modifier: Modifier) =
    ConstraintLayout(modifier.fillMaxWidth().wrapContentHeight().zIndex(17f)) {
        val (poster, title, originalTitle, genres, specs, rateStars, overview) = createRefs()
        val startGuideline = createGuidelineFromStart(16.dp)
        val endGuideline = createGuidelineFromEnd(16.dp)
        Card(
            elevation = 16.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(120.dp).constrainAs(poster) {
                top.linkTo(parent.top)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
        ) {
            CoilImage(data = movieDetail.posterUrl, contentScale = ContentScale.FillWidth)
        }

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
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
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
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
        )

        Text(
            text = movieDetail.genres.take(4).map(Genre::name).joinToString(),
            style = MaterialTheme.typography.subtitle1.copy(letterSpacing = 2.sp),
            modifier = Modifier.constrainAs(genres) {
                top.linkTo(originalTitle.bottom, 12.dp)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
        )

        MovieFields(movieDetail, modifier = Modifier.constrainAs(specs) {
            top.linkTo(genres.bottom, 12.dp)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
        })

        RateStars(movieDetail.voteAverage, modifier = Modifier.constrainAs(rateStars) {
            top.linkTo(specs.bottom, 12.dp)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
        })

        Text(
            text = movieDetail.overview,
            style = MaterialTheme.typography.body2.copy(
                letterSpacing = 2.sp,
                lineHeight = 30.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.padding(horizontal = 24.dp).constrainAs(overview) {
                top.linkTo(rateStars.bottom, 32.dp)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            })
    }

@Composable
private fun RateStars(voteAverage: Double, modifier: Modifier) {
    SpacedRow(spaceBetween = 4.dp, modifier) {
        val maxVote = 10
        val starCount = 5
        repeat(starCount) { starIndex ->
            val voteStarCount = voteAverage / (maxVote / starCount)
            val (tint, asset) = when {
                voteStarCount >= starIndex + 1 -> {
                    Color.Magenta to Icons.Filled.Star
                }
                voteStarCount in starIndex.toDouble()..(starIndex + 1).toDouble() -> {
                    Color.Magenta to Icons.Filled.StarHalf
                }
                else -> {
                    Color.DarkGray to Icons.Filled.StarOutline
                }
            }
            Icon(asset = asset, tint = tint)
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
