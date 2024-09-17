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

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val config = input.config ?: Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(minSize, minSize, config)
        val top = if (input.height == input.width) 0f else -24f

        output.applyCanvas {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, top, paint)
        }

        return output
    }
}
