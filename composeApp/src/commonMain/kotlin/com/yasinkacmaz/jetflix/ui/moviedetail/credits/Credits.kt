package com.yasinkacmaz.jetflix.ui.moviedetail.credits

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material.icons.rounded.Woman

data class Credits(val cast: List<Person>, val crew: List<Person>)

data class Person(val name: String, val role: String, val profilePhotoUrl: String?, val gender: Gender, val id: Int)

enum class Gender { MALE, FEMALE }

val Gender.placeholderIcon
    get() = when (this) {
        Gender.MALE -> Icons.Rounded.Man
        Gender.FEMALE -> Icons.Rounded.Woman
    }
