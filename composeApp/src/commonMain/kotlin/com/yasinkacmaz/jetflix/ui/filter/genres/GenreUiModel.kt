package com.yasinkacmaz.jetflix.ui.filter.genres

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class GenreUiModel(val id: Int = -1, val name: String = "")
