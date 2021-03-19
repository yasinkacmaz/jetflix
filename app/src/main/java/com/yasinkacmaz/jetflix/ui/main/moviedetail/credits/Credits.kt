package com.yasinkacmaz.jetflix.ui.main.moviedetail.credits

import androidx.annotation.DrawableRes
import com.yasinkacmaz.jetflix.R

data class Credits(val cast: List<Person>, val crew: List<Person>)

data class Person(val name: String, val role: String, val profilePhotoUrl: String?, val gender: Gender)

enum class Gender { MALE, FEMALE }

@DrawableRes
fun Gender.toPlaceholderImageRes() = when (this) {
    Gender.MALE -> R.drawable.ic_man
    Gender.FEMALE -> R.drawable.ic_woman
}
