package com.yasinkacmaz.jetflix.ui.main.genres

import androidx.compose.ui.graphics.Color
import com.yasinkacmaz.jetflix.data.Genre
import com.yasinkacmaz.jetflix.util.randomColor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class GenreUiModel(
    val genre: Genre = Genre(-1, ""),
    @Transient val primaryColor: Color = Color.randomColor(),
    @Transient val secondaryColor: Color = Color.randomColor()
)
