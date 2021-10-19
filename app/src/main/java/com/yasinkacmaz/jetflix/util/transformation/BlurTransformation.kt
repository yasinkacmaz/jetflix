@file:Suppress("DEPRECATION")

package com.yasinkacmaz.jetflix.util.transformation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.graphics.applyCanvas
import coil.size.Size
import coil.transform.Transformation

/**
 * A [Transformation] that applies a Gaussian blur to an image.
 *
 * @param context The [Context] used to create a [RenderScript] instance.
 * @param radius The radius of the blur.
 * @param sampling The sampling multiplier used to scale the image. Values > 1
 *  will downscale the image. Values between 0 and 1 will upscale the image.
 */
class BlurTransformation @JvmOverloads constructor(
    private val context: Context,
    private val radius: Float = DEFAULT_RADIUS,
    private val sampling: Float = DEFAULT_SAMPLING
) : Transformation {

    init {
        require(radius in 0.0..25.0) { "radius must be in [0, 25]." }
        require(sampling > 0) { "sampling must be > 0." }
        require(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            buildString {
                append("Please use blur modifier that comes with Jetpack Compose.")
                append("Starting with Android 12, RenderScript and related functionality to create")
                append(" bitmap transformations is deprecated to take full advantage of the GPU.")
                append(" See: https://developer.android.com/guide/topics/renderscript/migrate")
            }
        }
    }

    override val cacheKey: String = BlurTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val scaledWidth = (input.width / sampling).toInt()
        val scaledHeight = (input.height / sampling).toInt()
        val output = Bitmap.createBitmap(scaledWidth, scaledHeight, input.config)
        output.applyCanvas {
            scale(1 / sampling, 1 / sampling)
            drawBitmap(input, 0f, 0f, paint)
        }

        var script: RenderScript? = null
        var tmpInt: Allocation? = null
        var tmpOut: Allocation? = null
        var blur: ScriptIntrinsicBlur? = null
        try {
            script = RenderScript.create(context)
            tmpInt = Allocation.createFromBitmap(
                script,
                output,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
            tmpOut = Allocation.createTyped(script, tmpInt.type)
            blur = ScriptIntrinsicBlur.create(script, Element.U8_4(script))
            blur.setRadius(radius)
            blur.setInput(tmpInt)
            blur.forEach(tmpOut)
            tmpOut.copyTo(output)
        } finally {
            script?.destroy()
            tmpInt?.destroy()
            tmpOut?.destroy()
            blur?.destroy()
        }

        return output
    }

    private companion object {
        private const val DEFAULT_RADIUS = 10f
        private const val DEFAULT_SAMPLING = 1f
    }
}
