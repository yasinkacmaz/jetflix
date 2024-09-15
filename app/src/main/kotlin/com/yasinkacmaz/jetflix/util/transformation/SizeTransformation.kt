package com.yasinkacmaz.jetflix.util.transformation

import android.graphics.Bitmap
import androidx.annotation.IntRange
import coil.size.Size
import coil.transform.Transformation

class SizeTransformation(@IntRange(from = 0, to = 100) val percent: Int) : Transformation {

    override val cacheKey: String = SizeTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val newWidth = input.width * percent / 100
        val newHeight = input.height * percent / 100
        return Bitmap.createScaledBitmap(input, newWidth, newHeight, false)
    }
}
