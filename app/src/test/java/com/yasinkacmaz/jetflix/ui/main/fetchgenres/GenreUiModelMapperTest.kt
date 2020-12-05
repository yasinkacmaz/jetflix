package com.yasinkacmaz.jetflix.ui.main.fetchgenres

import com.yasinkacmaz.jetflix.data.Genre
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class GenreUiModelMapperTest {
    @Test
    fun map() {
        val mapper = GenreUiModelMapper()
        val input = Genre(1, "Name")

        val uiModel = mapper.map(input)

        expectThat(uiModel.genre).isEqualTo(input)
    }
}
