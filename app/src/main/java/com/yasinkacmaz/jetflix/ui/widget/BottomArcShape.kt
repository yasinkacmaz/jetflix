package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

class BottomArcShape(private val arcHeight: Float) : Shape {
    override fun createOutline(size: Size, density: Density): Outline {
        val path = Path().apply {
            moveTo(size.width, 0f)
            lineTo(size.width, size.height)
            val arcOffset = arcHeight / 4
            val rect = Rect(
                0f - arcOffset,
                size.height - arcHeight,
                size.width + arcOffset,
                size.height
            )
            arcTo(rect, 0f, 180f, false)
            lineTo(0f, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview
fun BottomArcShapePreview() {
    val shape = BottomArcShape(100.dp.value * AmbientDensity.current.density)
    Box(Modifier.size(200.dp, 300.dp).background(color = Color.Magenta, shape = shape))
}
