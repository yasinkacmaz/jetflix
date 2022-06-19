package com.yasinkacmaz.jetflix.ui.moviedetail.image

import com.yasinkacmaz.jetflix.data.ImagesResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toOriginalUrl
import javax.inject.Inject

class ImageMapper @Inject constructor() : Mapper<ImagesResponse, List<Image>> {
    override fun map(input: ImagesResponse): List<Image> {
        return buildList {
            addAll(input.backdrops.map { Image(it.filePath.toOriginalUrl(), it.voteCount) })
            addAll(input.posters.map { Image(it.filePath.toOriginalUrl(), it.voteCount) })
            sortByDescending { it.voteCount }
        }
    }
}
