package com.yasinkacmaz.jetflix.ui.moviedetail.credits

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material.icons.rounded.Woman
import androidx.compose.runtime.Stable

data class Credits(@Stable val cast: List<Person>, @Stable val crew: List<Person>)

data class Person(val name: String, val role: String, val profilePhotoUrl: String?, val gender: Gender)

enum class Gender { MALE, FEMALE }

val Gender.placeholderIcon
    get() = when (this) {
        Gender.MALE -> Icons.Rounded.Man
        Gender.FEMALE -> Icons.Rounded.Woman
    }
