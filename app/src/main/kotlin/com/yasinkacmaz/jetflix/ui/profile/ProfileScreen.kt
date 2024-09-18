package com.yasinkacmaz.jetflix.ui.profile

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
import com.yasinkacmaz.jetflix.util.openInChromeCustomTab

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val uiState = profileViewModel.uiState.collectAsState().value

    when {
        uiState.loading -> {
            LoadingColumn(stringResource(id = R.string.fetching_profile))
        }

        uiState.error != null -> {
            ErrorColumn(uiState.error.message.orEmpty())
        }

        uiState.profile != null -> {
            Profile(uiState.profile)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Profile(profile: Profile) {
    val lazyListState = rememberLazyListState()
    val isTitleStuck by remember {
        derivedStateOf {
            val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            val firstVisibleItemOffset = lazyListState.firstVisibleItemScrollOffset
            firstVisibleItemIndex > 0 && firstVisibleItemOffset != 0
        }
    }
    val defaultVibrantColor = MaterialTheme.colorScheme.onSurface
    val vibrantColor = remember { Animatable(defaultVibrantColor) }
    GetVibrantColorFromPoster(profile.profilePhotoUrl, vibrantColor)
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .alpha(animateFloatAsState(if (isTitleStuck) 1f else 0f, label = "").value)
                    .shadow(8.dp),
                title = { Name(profile.name) },
            )
        },
    ) {
        LazyColumn(state = lazyListState) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = LocalConfiguration.current.screenHeightDp.dp * 0.4f)
                        .background(MaterialTheme.colorScheme.surface)
                        .animateContentSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profile.profilePhotoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.TopCenter,
                )
            }

            item {
                TopAppBar(
                    windowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0),
                    modifier = Modifier.alpha(animateFloatAsState(if (isTitleStuck) 0f else 1f, label = "").value),
                    title = { Name(profile.name) },
                )
            }

            item {
                ImdbProfileButton(profile.imdbProfileUrl, vibrantColor.value)
            }

            item {
                ProfileField(R.string.birthday, profile.birthday)
            }

            item {
                ProfileField(R.string.birthplace, profile.placeOfBirth)
            }

            item {
                ProfileField(R.string.known_for, profile.knownFor)
            }

            item {
                AlsoKnownAs(profile.alsoKnownAs, vibrantColor.value)
            }

            item {
                Text(
                    text = profile.biography,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(MaterialTheme.spacing.l),
                )
            }
        }
    }
}

@Composable
private fun Name(name: String) = Text(
    text = name,
    style = MaterialTheme.typography.headlineMedium.copy(
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.5.sp,
        fontFamily = FontFamily.Cursive,
    ),
    maxLines = 2,
    overflow = TextOverflow.Ellipsis,
)

@Composable
private fun ProfileField(@StringRes resId: Int, field: String) {
    if (field.isEmpty()) return

    Text(
        stringResource(resId, field),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
    )
}

@Composable
private fun AlsoKnownAs(alsoKnownAs: List<String>, vibrantColor: Color) {
    if (alsoKnownAs.isEmpty()) return

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
    ) {
        item {
            Text(stringResource(R.string.also_known_as), style = MaterialTheme.typography.bodyLarge)
        }
        items(alsoKnownAs) {
            Text(
                it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .border(1.25.dp, vibrantColor, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.s, vertical = MaterialTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun ImdbProfileButton(imdbProfileUrl: String?, currentVibrantColor: Color) {
    if (imdbProfileUrl.isNullOrBlank()) return

    val context = LocalContext.current
    Row(
        Modifier
            .clickable { imdbProfileUrl.openInChromeCustomTab(context, currentVibrantColor) }
            .padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.OpenInNew,
            contentDescription = stringResource(id = R.string.open_imdb_content_description),
            tint = currentVibrantColor,
            modifier = Modifier.scale(1.1f),
        )
        Text(
            stringResource(R.string.open_imdb_profile),
            Modifier.padding(start = MaterialTheme.spacing.s),
            color = currentVibrantColor,
        )
    }
}

@Preview(showSystemUi = true, name = "Light")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun ProfilePreview() {
    val profile = Profile(
        name = "Yasin Kaçmaz",
        biography = "Yasin Kaçmaz, Turkish actor and developer.",
        birthday = "06.12.1994",
        placeOfBirth = "Istanbul",
        alsoKnownAs = listOf("Yasin"),
        imdbProfileUrl = "https://github.com/yasinkacmaz/jetflix",
        profilePhotoUrl = "it is not working :(",
        knownFor = "Development, Acting",
    )
    Profile(profile)
}
