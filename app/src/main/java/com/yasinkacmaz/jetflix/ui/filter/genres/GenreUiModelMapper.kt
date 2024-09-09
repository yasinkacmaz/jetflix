package com.yasinkacmaz.jetflix.ui.filter.genres

import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.util.Mapper

class GenreUiModelMapper : Mapper<Genre, GenreUiModel> {
    override fun map(input: Genre) = GenreUiModel(input)
}
