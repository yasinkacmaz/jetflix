package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

enum class ItemState { PLACING, PLACED }

data class ItemAnimationArgs(
    val scaleRange: ClosedRange<Float>,
    val alphaRange: ClosedRange<Float>,
    val durationMillis: Int = 400,
    val delay: Int = 0,
    val easing: Easing = FastOutSlowInEasing,
)

data class ItemAnimationData(val scale: Float, val alpha: Float)

@Composable
fun animateItem(args: ItemAnimationArgs): ItemAnimationData = with(args) {
    val transitionState = remember {
        MutableTransitionState(ItemState.PLACING).apply {
            targetState = ItemState.PLACED
        }
    }
    val animationSpec =
        tween<Float>(durationMillis = durationMillis, delayMillis = delay, easing = easing)
    val label = "itemPlacement"
    val transition = updateTransition(transitionState, label = label)

    val scale by transition.animateFloat(
        transitionSpec = { animationSpec },
        label = "$label-Scale",
    ) { state ->
        when (state) {
            ItemState.PLACING -> scaleRange.start
            ItemState.PLACED -> scaleRange.endInclusive
        }
    }
    val alpha by transition.animateFloat(
        transitionSpec = { animationSpec },
        label = "$label-Alpha",
    ) { state ->
        when (state) {
            ItemState.PLACING -> alphaRange.start
            ItemState.PLACED -> alphaRange.endInclusive
        }
    }
    return ItemAnimationData(scale = scale, alpha = alpha)
}
