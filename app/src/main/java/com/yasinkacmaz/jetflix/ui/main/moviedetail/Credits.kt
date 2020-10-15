package com.yasinkacmaz.jetflix.ui.main.moviedetail

import androidx.annotation.DrawableRes
import com.yasinkacmaz.jetflix.R

data class Credits(val cast: List<Cast>, val crew: List<Crew>)

data class Cast(val name: String, val character: String, val profilePhotoUrl: String?, val gender: Gender)

data class Crew(val name: String, val job: String, val profilePhotoUrl: String?, val gender: Gender)

enum class Gender { MALE, FEMALE }

@DrawableRes
fun Gender.toPlaceholderImageRes() = when(this) {
    Gender.MALE -> R.drawable.ic_male
    Gender.FEMALE -> R.drawable.ic_female
}
