package com.yasinkacmaz.jetflix.ui.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yasinkacmaz.jetflix.LocalNavController
import com.yasinkacmaz.jetflix.ui.common.Error
import com.yasinkacmaz.jetflix.ui.common.Loading
import com.yasinkacmaz.jetflix.ui.theme.spacing
import com.yasinkacmaz.jetflix.ui.widget.CircleIconButton
import com.yasinkacmaz.jetflix.util.JetflixImage
import com.yasinkacmaz.jetflix.util.openInBrowser
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.also_known_as
import jetflix.composeapp.generated.resources.back
import jetflix.composeapp.generated.resources.birthday
import jetflix.composeapp.generated.resources.birthplace
import jetflix.composeapp.generated.resources.fetching_profile
import jetflix.composeapp.generated.resources.known_for
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
            Error(message = uiState.error.message.orEmpty())
        }

        uiState.profile != null -> {
            Profile(uiState.profile)
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
private fun Profile(profile: Profile) {
    val navController = LocalNavController.current
    val screenSize = LocalWindowInfo.current.containerSize
    val screenSizeDp = with(LocalDensity.current) {
        DpSize(width = screenSize.width.toDp(), height = screenSize.height.toDp())
    }
    val windowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(screenSizeDp)
    val imageMinimumHeight = with(LocalDensity.current) { (screenSize.height * 0.45f).toDp() }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding().padding(horizontal = MaterialTheme.spacing.l),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = { },
                navigationIcon = {
                    CircleIconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
                actions = {
                    val uriHandler = LocalUriHandler.current
                    CircleIconButton(onClick = { profile.imdbProfileUrl?.openInBrowser(uriHandler) }) {
                        Icon(Icons.Default.Language, contentDescription = null)
                    }
                },
            )
        },
    ) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = windowSizeClass.widthSizeClass,
                contentKey = { if (it == Compact) "ProfileSmallUI" else "ProfileLargeUI" },
            ) {
                when (it) {
                    Compact -> {
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                            JetflixImage(
                                data = profile.profilePhotoUrl,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(imageMinimumHeight)
                                    .sharedBounds(
                                        rememberSharedContentState(key = "profileImage"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                    ),
                            )
                            ProfileInformation(
                                profile,
                                modifier = Modifier.sharedElement(
                                    rememberSharedContentState(key = "profileInformation"),
                                    animatedVisibilityScope = this@AnimatedContent,
                                ),
                            )
                        }
                    }

                    Medium, Expanded -> {
                        Row(modifier = Modifier.fillMaxSize()) {
                            JetflixImage(
                                data = profile.profilePhotoUrl,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .sharedBounds(
                                        rememberSharedContentState(key = "profileImage"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                    ),
                            )
                            Column(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                ProfileInformation(
                                    profile,
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(key = "profileInformation"),
                                        animatedVisibilityScope = this@AnimatedContent,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInformation(profile: Profile, modifier: Modifier) {
    Column(modifier) {
        Name(profile.name)
        ProfileField(Res.string.birthday, profile.birthday)
        ProfileField(Res.string.birthplace, profile.placeOfBirth)
        ProfileField(Res.string.known_for, profile.knownFor)
        AlsoKnownAs(profile.alsoKnownAs)
        Biography(profile)
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
    modifier = Modifier.padding(
        horizontal = MaterialTheme.spacing.l,
        vertical = MaterialTheme.spacing.xs,
    ),
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
private fun AlsoKnownAs(alsoKnownAs: List<String>) {
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
                    .border(1.25.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50))
                    .padding(horizontal = MaterialTheme.spacing.s, vertical = MaterialTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun Biography(profile: Profile) = Text(
    text = profile.biography,
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.padding(MaterialTheme.spacing.l),
)
