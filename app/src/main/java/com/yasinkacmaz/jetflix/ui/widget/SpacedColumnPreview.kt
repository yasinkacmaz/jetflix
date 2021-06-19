package com.yasinkacmaz.jetflix.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SpacedColumnProvider : CollectionPreviewParameterProvider<Int>(listOf(0, 60))

@Composable
@Preview
fun EyeTestPreview(@PreviewParameter(SpacedColumnProvider::class) spaceBetween: Int) {
    SpacedColumn(spaceBetween.dp, Modifier.background(Color.White)) {
        var fontSize = 64
        fun reduceFont(): Int {
            fontSize /= 2
            return fontSize
        }

        val style = TextStyle(
            color = Color.DarkGray,
            fontSize = fontSize.sp,
            textAlign = Center,
            letterSpacing = (fontSize / 2).sp
        )
        val modifier = Modifier.fillMaxWidth()
        Text(text = "EFTRH", modifier = modifier, style = style)
        Text(text = "RTPND", modifier = modifier, style = style.copy(fontSize = reduceFont().sp))
        Text(text = "PUHDF", modifier = modifier, style = style.copy(fontSize = reduceFont().sp))
        Text(text = "FPUND", modifier = modifier, style = style.copy(fontSize = reduceFont().sp))
        Text(text = "NPDFT", modifier = modifier, style = style.copy(fontSize = reduceFont().sp))
        Text(text = "HDREP", modifier = modifier, style = style.copy(fontSize = reduceFont().sp))
    }
}
