package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private enum class ScaleState { DEFAULT, SCALED }

@Composable
fun scale(toScale: Float, animation: FiniteAnimationSpec<Float>): Float {
    val label = "scale"
    val scaleState = remember {
        MutableTransitionState(ScaleState.DEFAULT).apply {
            targetState = ScaleState.SCALED
        }
    }
    val scaleTransition = updateTransition(scaleState, label = label)

    val scale by scaleTransition.animateFloat(
        transitionSpec = { animation },
        label = label,
    ) { state ->
        when (state) {
            ScaleState.DEFAULT -> 1f
            ScaleState.SCALED -> toScale
        }
    }
    return scale
}

@Preview(name = "Run on device to see scale animation")
@Composable
private fun ScaleAnimationPreview() {
    val scale = scale(toScale = 3f, animation = springAnimation)
    ScalingDot(scale = scale)
}

@Preview(name = "Run on device to see infinite pulse animation")
@Composable
private fun ScalePulseAnimationPreview() {
    val animatedScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        animatedScale.animateTo(targetValue = 5f, animationSpec = defaultScaleAnimation)
    }
    ScalingDot(scale = animatedScale.value)
}

@Composable
private fun ScalingDot(scale: Float) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = Color.Red,
            shape = CircleShape,
            modifier = Modifier
                .wrapContentSize()
                .size(60.dp)
                .scale(scale),
        ) {
        }
    }
}
