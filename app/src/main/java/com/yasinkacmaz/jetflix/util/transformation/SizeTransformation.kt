package com.yasinkacmaz.jetflix.util.transformation

import android.graphics.Bitmap
import androidx.annotation.IntRange
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation

class SizeTransformation(@IntRange(from = 0, to = 100) val percent: Int) : Transformation {

    override fun key(): String = SizeTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val newWidth = input.width * percent / 100
        val newHeight = input.height * percent / 100
        return Bitmap.createScaledBitmap(input, newWidth, newHeight, false)
    }

    override fun equals(other: Any?) = other is SizeTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "SizeTransformation()"
}
