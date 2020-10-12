package com.yasinkacmaz.jetflix.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp

@Composable
fun Int.dpToPx(): Float {
    val density = DensityAmbient.current.density
    return remember(this) { this * density }
}

@Composable
fun Dp.toPx(): Float {
    val density = DensityAmbient.current.density
    return remember(this.value) { this.value * density }
}
