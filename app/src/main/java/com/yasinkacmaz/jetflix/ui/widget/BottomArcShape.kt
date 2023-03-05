package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.util.dpToPx

class BottomArcShape(private val arcHeight: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            moveTo(size.width, 0f)
            lineTo(size.width, size.height)
            val arcOffset = arcHeight / 10
            val rect = Rect(
                left = 0f - arcOffset,
                top = size.height - arcHeight,
                right = size.width + arcOffset,
                bottom = size.height,
            )
            arcTo(rect, 0f, 180f, false)
            lineTo(0f, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun BottomArcShapePreview() {
    val shape = BottomArcShape(240.dpToPx())
    Surface(
        Modifier
            .wrapContentSize()
            .padding(32.dp),
    ) {
        Box(
            Modifier
                .size(320.dp, 480.dp)
                .background(color = Color.Magenta, shape = shape),
        )
    }
}
