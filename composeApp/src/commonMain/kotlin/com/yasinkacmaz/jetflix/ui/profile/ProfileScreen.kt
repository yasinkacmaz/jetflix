package com.yasinkacmaz.jetflix.ui.profile

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.openInBrowser
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.also_known_as
import jetflix.composeapp.generated.resources.birthday
import jetflix.composeapp.generated.resources.birthplace
import jetflix.composeapp.generated.resources.fetching_profile
import jetflix.composeapp.generated.resources.known_for
import jetflix.composeapp.generated.resources.open_imdb_content_description
import jetflix.composeapp.generated.resources.open_imdb_profile
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val uiState = profileViewModel.uiState.collectAsState().value

    when {
        uiState.loading -> {
            Loading(modifier = Modifier.fillMaxSize(), title = stringResource(Res.string.fetching_profile))
        }

        uiState.error != null -> {
            Error(uiState.error.message.orEmpty())
        }

        uiState.profile != null -> {
            Profile(uiState.profile)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                JetflixImage(
                    data = profile.profilePhotoUrl,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.heightIn(min = LocalConfiguration.current.screenHeightDp.dp * 0.4f)
                        .background(MaterialTheme.colorScheme.surface)
                        .animateContentSize(),
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
                ProfileField(Res.string.birthday, profile.birthday)
            }

            item {
                ProfileField(Res.string.birthplace, profile.placeOfBirth)
            }

            item {
                ProfileField(Res.string.known_for, profile.knownFor)
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
private fun ProfileField(resource: StringResource, field: String) {
    if (field.isEmpty()) return

    Text(
        stringResource(resource, field),
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
            Text(stringResource(Res.string.also_known_as), style = MaterialTheme.typography.bodyLarge)
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

    val uriHandler = LocalUriHandler.current
    Row(
        Modifier
            .clickable { imdbProfileUrl.openInBrowser(uriHandler) }
            .padding(horizontal = MaterialTheme.spacing.l, vertical = MaterialTheme.spacing.xs),
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.OpenInNew,
            contentDescription = stringResource(Res.string.open_imdb_content_description),
            tint = currentVibrantColor,
            modifier = Modifier.scale(1.1f),
        )
        Text(
            stringResource(Res.string.open_imdb_profile),
            Modifier.padding(start = MaterialTheme.spacing.s),
            color = currentVibrantColor,
        )
    }
}
