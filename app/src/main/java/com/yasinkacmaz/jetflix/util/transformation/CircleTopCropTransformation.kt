package com.yasinkacmaz.jetflix.util.transformation

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class CircleTopCropTransformation : Transformation {

    override fun key(): String = CircleTopCropTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val output = pool.get(minSize, minSize, input.config)
        val top = if (input.height == input.width) 0f else -30f
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = XFERMODE
            drawBitmap(input, radius - input.width / 2f, top, paint)
        }

        return output
    }

    private companion object {
        val XFERMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
}
