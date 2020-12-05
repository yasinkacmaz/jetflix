package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.util.randomColor

val AmbientSelectedGenre = staticAmbientOf<MutableState<GenreUiModel>> { error("No genre available") }

data class GenreUiModel(
    val genre: Genre = Genre(-1, ""),
    val primaryColor: Color = Color.randomColor(),
    val secondaryColor: Color = Color.randomColor()
)
