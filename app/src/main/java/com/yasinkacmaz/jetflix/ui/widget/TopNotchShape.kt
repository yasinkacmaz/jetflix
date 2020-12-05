package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.yasinkacmaz.jetflix.util.dpToPx

/**
 * 1------2   5------6
 * |      |   |      |
 * |      3---4      |
 * |        -        |
 * |        +        |
 * 8-----------------7
 */
class TopNotchShape(private val notchSize: Size, private val cornerRadius: Float) : Shape {
    override fun createOutline(size: Size, density: Density): Outline {
        val widthMinusCorners = size.width - (2 * cornerRadius)
        val heightMinusCorners = size.height - (2 * cornerRadius)
        val notchSideWidth = (size.width - (4 * cornerRadius) - notchSize.width) / 2
        val notchHeightMinusCorners = notchSize.height - (2 * cornerRadius)
        val notchWidthMinusCorners = notchSize.width - (2 * cornerRadius)
        val path = Path().apply {
            moveTo(0f, 0f + cornerRadius)
            relativeQuadraticBezierTo(0f, -cornerRadius, cornerRadius, -cornerRadius) // 1
            relativeLineTo(notchSideWidth, 0f)
            relativeQuadraticBezierTo(cornerRadius, 0f, cornerRadius, cornerRadius) // 2
            relativeLineTo(0f, notchHeightMinusCorners)
            relativeQuadraticBezierTo(0f, cornerRadius, cornerRadius, cornerRadius) // 3
            relativeLineTo(notchWidthMinusCorners, 0f)
            relativeQuadraticBezierTo(cornerRadius, 0f, cornerRadius, -cornerRadius) // 4
            relativeLineTo(0f, -notchHeightMinusCorners)
            relativeQuadraticBezierTo(0f, -cornerRadius, cornerRadius, -cornerRadius) // 5
            relativeLineTo(notchSideWidth, 0f)
            relativeQuadraticBezierTo(cornerRadius, 0f, cornerRadius, cornerRadius) // 6
            relativeLineTo(0f, heightMinusCorners)
            relativeQuadraticBezierTo(0f, cornerRadius, -cornerRadius, cornerRadius) // 7
            relativeLineTo(-widthMinusCorners, 0f)
            relativeQuadraticBezierTo(-cornerRadius, 0f, -cornerRadius, -cornerRadius) // 8
            relativeLineTo(0f, -heightMinusCorners)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview
fun TopNotchShapePreview() {
    Surface(
        shape = TopNotchShape(Size(160.dpToPx(), 80.dpToPx()), 16.dpToPx()),
        color = Color.Magenta,
        modifier = Modifier.size(300.dp, 400.dp)
    ) {
    }
}
