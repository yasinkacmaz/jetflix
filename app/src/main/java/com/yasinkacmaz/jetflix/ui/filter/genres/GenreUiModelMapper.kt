package com.yasinkacmaz.jetflix.ui.filter.genres

import com.yasinkacmaz.jetflix.data.remote.Genre
import com.yasinkacmaz.jetflix.util.Mapper
import javax.inject.Inject

class GenreUiModelMapper @Inject constructor() : Mapper<Genre, GenreUiModel> {
    override fun map(input: Genre) = GenreUiModel(input)
}
