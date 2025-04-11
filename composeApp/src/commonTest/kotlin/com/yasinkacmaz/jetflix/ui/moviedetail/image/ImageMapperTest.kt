package com.yasinkacmaz.jetflix.ui.moviedetail.image

import com.yasinkacmaz.jetflix.data.remote.ImagesResponse
import com.yasinkacmaz.jetflix.util.resource.imagesJson
import com.yasinkacmaz.jetflix.util.parseJson
import com.yasinkacmaz.jetflix.util.toOriginalUrl
import io.kotest.matchers.collections.shouldContainAll
import kotlin.test.Test

class ImageMapperTest {
    private val mapper = ImageMapper()

    private val imagesResponse: ImagesResponse = parseJson(imagesJson)

    @Test
    fun `Map backdrops and posters with original url`() {
        val images = mapper.map(imagesResponse)

        val expectedBackdrops = imagesResponse.backdrops.map { Image(it.filePath.toOriginalUrl(), it.voteCount) }
        val expectedPosters = imagesResponse.posters.map { Image(it.filePath.toOriginalUrl(), it.voteCount) }
        images shouldContainAll expectedBackdrops
        images shouldContainAll expectedPosters
    }
}
