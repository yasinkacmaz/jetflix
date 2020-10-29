package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

enum class ScaleState { DEFAULT, SCALED }

enum class AnimationType { TRANSITION, ANIMATE, ANIMATEDVALUE }

class ScaleAnimation(private val toScale: Float, private val animation: AnimationSpec<Float>) {
    private val scale = FloatPropKey()

    private val transitionDefinition = transitionDefinition<ScaleState> {

        state(ScaleState.DEFAULT) {
            this[scale] = 1f
        }

        state(ScaleState.SCALED) {
            this[scale] = toScale
        }

        transition(fromState = ScaleState.DEFAULT, toState = ScaleState.SCALED) {
            scale using animation
        }
    }

    @Composable
    private fun getTransition() = transition(
        definition = transitionDefinition,
        initState = ScaleState.DEFAULT,
        toState = ScaleState.SCALED
    )

    @Composable
    fun scale() = getTransition()[scale]
}

private val defaultScaleAnimation: AnimationSpec<Float> = repeatable(
    AnimationConstants.Infinite,
    tween(3000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse
)

@Preview(name = "Use animation preview")
@Composable
private fun ScalePulseAnimationPreview() {
    val anim = remember { ScaleAnimation(toScale = 3f, animation = defaultScaleAnimation) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Red,
            shape = CircleShape,
            modifier = Modifier.size(60.dp).drawLayer(scaleX = anim.scale(), scaleY = anim.scale())
        ) {

        }

    }
}

@Preview(name = "Run on device to see animation")
@Composable
private fun ScalePulseAnimationPreview2() {
    val animation: AnimationSpec<Float> = repeatable(
        AnimationConstants.Infinite,
        tween(3000, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
    )
    val animatedScale = animatedFloat(1f)
    onActive {
        animatedScale.animateTo(targetValue = 2f, anim = animation)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Red,
            shape = CircleShape,
            modifier = Modifier.size(60.dp).drawLayer(scaleX = animatedScale.value, scaleY = animatedScale.value)
        ) {

        }

    }
}
