package com.yasinkacmaz.jetflix.ui.filter.genres

import com.yasinkacmaz.jetflix.data.remote.Genre
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class GenreUiModelMapperTest {
    @Test
    fun map() {
        val mapper = GenreUiModelMapper()
        val input = Genre(1, "Name")

        val uiModel = mapper.map(input)

        uiModel.genre shouldBe input
    }
}
