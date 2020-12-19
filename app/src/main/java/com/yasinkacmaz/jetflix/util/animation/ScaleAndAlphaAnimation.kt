package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private enum class State { PLACING, PLACED }

data class ScaleAndAlphaArgs(val fromScale: Float, val toScale: Float, val fromAlpha: Float, val toAlpha: Float)

class ScaleAndAlphaAnimation(private val args: ScaleAndAlphaArgs, private val animation: AnimationSpec<Float>) {
    private val scale = FloatPropKey()
    private val alpha = FloatPropKey()

    private val transitionDefinition = transitionDefinition<State> {

        state(State.PLACING) {
            this[scale] = args.fromScale
            this[alpha] = args.fromAlpha
        }

        state(State.PLACED) {
            this[scale] = args.toScale
            this[alpha] = args.toAlpha
        }

        transition(fromState = State.PLACING, toState = State.PLACED) {
            scale using animation
            alpha using animation
        }
    }

    @Composable
    private fun getTransition() = transition(
        definition = transitionDefinition,
        initState = State.PLACING,
        toState = State.PLACED
    )

    @Composable
    fun scaleAndAlpha() = getTransition()[scale] to getTransition()[alpha]
}

@Preview
@Composable
private fun ScaleAndAlphaAnimationPreview() {
    val anim = remember {
        ScaleAndAlphaAnimation(
            args = ScaleAndAlphaArgs(
                fromScale = 2f,
                toScale = 1f,
                fromAlpha = 0f,
                toAlpha = 1f
            ),
            animation = defaultScaleAnimation
        )
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Red,
            shape = CircleShape,
            modifier = Modifier.size(60.dp).graphicsLayer(
                scaleX = anim.scaleAndAlpha().first,
                scaleY = anim.scaleAndAlpha().first,
                alpha = anim.scaleAndAlpha().second
            )
        ) {
        }
    }
}
