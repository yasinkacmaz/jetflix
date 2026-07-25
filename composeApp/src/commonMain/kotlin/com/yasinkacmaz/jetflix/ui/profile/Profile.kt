package com.yasinkacmaz.jetflix.ui.profile

import androidx.compose.runtime.Immutable

@Immutable
data class Profile(
    val name: String,
    val biography: String,
    val birthday: String,
    val placeOfBirth: String,
    val alsoKnownAs: List<String> = emptyList(),
    val imdbProfileUrl: String?,
    val profilePhotoUrl: String,
    val knownFor: String,
)
