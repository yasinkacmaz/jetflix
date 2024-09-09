package com.yasinkacmaz.jetflix.ui.moviedetail.image

import com.yasinkacmaz.jetflix.data.remote.ImagesResponse
import com.yasinkacmaz.jetflix.util.Mapper
import com.yasinkacmaz.jetflix.util.toOriginalUrl

class ImageMapper : Mapper<ImagesResponse, List<Image>> {
    override fun map(input: ImagesResponse): List<Image> {
        return buildList {
            addAll(input.backdrops.map { Image(it.filePath.toOriginalUrl(), it.voteCount) })
            addAll(input.posters.map { Image(it.filePath.toOriginalUrl(), it.voteCount) })
            sortByDescending { it.voteCount }
        }
    }
}
