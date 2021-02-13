package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private enum class State { PLACING, PLACED }

data class ScaleAndAlphaArgs(val fromScale: Float, val toScale: Float, val fromAlpha: Float, val toAlpha: Float)

@Composable
fun scaleAndAlpha(args: ScaleAndAlphaArgs, animation: FiniteAnimationSpec<Float>): Pair<Float, Float> {
    val transitionState = remember { MutableTransitionState(State.PLACING).apply { targetState = State.PLACED } }
    val transition = updateTransition(transitionState)
    val alpha by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> {
                args.fromAlpha
            }
            State.PLACED -> {
                args.toAlpha
            }
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animation }) { state ->
        when (state) {
            State.PLACING -> {
                args.fromScale
            }
            State.PLACED -> {
                args.toScale
            }
        }
    }
    return alpha to scale
}

@Preview
@Composable
private fun ScaleAndAlphaAnimationPreview() {
    val anim = scaleAndAlpha(
        args = ScaleAndAlphaArgs(
            fromScale = 2f,
            toScale = 1f,
            fromAlpha = 0f,
            toAlpha = 1f
        ),
        animation = springAnimation
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Red,
            shape = CircleShape,
            modifier = Modifier.size(60.dp).graphicsLayer(
                scaleX = anim.first,
                scaleY = anim.first,
                alpha = anim.second
            )
        ) {
        }
    }
}
