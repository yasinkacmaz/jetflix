package com.yasinkacmaz.jetflix.ui.moviedetail.credits

import androidx.compose.runtime.Immutable
import jetflix.composeapp.generated.resources.Res
import jetflix.composeapp.generated.resources.man
import jetflix.composeapp.generated.resources.woman
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class Credits(val cast: List<Person>, val crew: List<Person>)

@Immutable
data class Person(val name: String, val role: String, val profilePhotoUrl: String?, val gender: Gender, val id: Int)

enum class Gender { MALE, FEMALE }

val Gender.placeholderIcon: DrawableResource
    get() = when (this) {
        Gender.MALE -> Res.drawable.man
        Gender.FEMALE -> Res.drawable.woman
    }
