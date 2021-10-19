package com.yasinkacmaz.jetflix.util.transformation

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class CircleTopCropTransformation : Transformation {

    override val cacheKey: String = CircleTopCropTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val output = Bitmap.createBitmap(minSize, minSize, input.config)
        val top = if (input.height == input.width) 0f else -20f
        return output.applyCanvas {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = MODE
            drawBitmap(input, radius - input.width / 2f, top, paint)
        }
    }

    companion object {
        private val MODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
}
