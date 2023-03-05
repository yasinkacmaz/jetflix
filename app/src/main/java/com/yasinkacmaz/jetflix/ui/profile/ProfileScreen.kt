package com.yasinkacmaz.jetflix.ui.profile

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yasinkacmaz.jetflix.R
import com.yasinkacmaz.jetflix.ui.common.error.ErrorColumn
import com.yasinkacmaz.jetflix.ui.common.loading.LoadingColumn
import com.yasinkacmaz.jetflix.ui.theme.JetflixTheme
import com.yasinkacmaz.jetflix.util.GetVibrantColorFromPoster
import com.yasinkacmaz.jetflix.util.openInChromeCustomTab

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val uiState = profileViewModel.uiState.collectAsState().value

    when {
        uiState.loading -> {
            val title = stringResource(id = R.string.fetching_profile)
            LoadingColumn(title)
        }
        uiState.error != null -> {
            ErrorColumn(uiState.error.message.orEmpty())
        }
        uiState.profile != null -> {
            Profile(uiState.profile)
        }
    }
}

@Composable
private fun Profile(profile: Profile) = JetflixTheme {
    val defaultVibrantColor = MaterialTheme.colors.onSurface
    val vibrantColor = remember { Animatable(defaultVibrantColor) }
    var screenHeight by remember { mutableStateOf(0) }
    var imageHeight by remember { mutableStateOf(0) }
    Surface(Modifier.fillMaxSize().onSizeChanged { screenHeight = it.height }) {
        AsyncImage(
            model = profile.profilePhotoUrl,
            contentDescription = null,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize().onSizeChanged { imageHeight = it.height },
        )
        GetVibrantColorFromPoster(profile.profilePhotoUrl, vibrantColor)
        val imageHeightToScreenHeightRatio = try {
            (imageHeight / screenHeight).toFloat().coerceIn(minimumValue = 0.4f, maximumValue = 0.7f)
        } catch (e: Exception) {
            0.4f
        }
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to vibrantColor.value.copy(alpha = 0f),
                        imageHeightToScreenHeightRatio - 0.05f to vibrantColor.value,
                    ),
                ),
        )
        Column(
            Modifier.fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        ) {
            Spacer(Modifier.fillMaxHeight(imageHeightToScreenHeightRatio - 0.2f))
            Text(
                text = profile.name,
                style = MaterialTheme.typography.h1.copy(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Cursive,
                    color = Color.White.copy(alpha = 0.8f),
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            ) {
                Column(
                    Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ProfileField(R.string.birthday, profile.birthday)
                    ProfileField(R.string.birthplace, profile.placeOfBirth)
                    ProfileField(R.string.known_for, profile.knownFor)
                    AlsoKnownAs(profile.alsoKnownAs, vibrantColor.value)
                    ImdbProfileButton(profile.imdbProfileUrl, vibrantColor.value)
                    Text(profile.biography, Modifier.padding(top = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileField(@StringRes resId: Int, field: String) {
    if (field.isEmpty()) return

    Text(stringResource(resId, field), style = MaterialTheme.typography.body1)
}

@Composable
private fun AlsoKnownAs(alsoKnownAs: List<String>, vibrantColor: Color) {
    if (alsoKnownAs.isEmpty()) return

    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        item {
            Text(stringResource(R.string.also_known_as), style = MaterialTheme.typography.body1)
        }
        items(alsoKnownAs) {
            Text(
                it,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .border(1.25.dp, vibrantColor, RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
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
            .padding(all = 4.dp),
    ) {
        Icon(
            Icons.Rounded.OpenInNew,
            contentDescription = stringResource(id = R.string.open_imdb_content_description),
            tint = currentVibrantColor,
            modifier = Modifier.scale(1.1f),
        )
        Text(
            stringResource(R.string.open_imdb_profile),
            Modifier.padding(start = 8.dp),
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
        imdbProfileUrl = "www",
        profilePhotoUrl = "it is not working :(",
        knownFor = "Development, Acting",
    )
    Profile(profile)
}
