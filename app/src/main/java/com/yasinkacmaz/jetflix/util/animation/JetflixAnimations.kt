package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.*

val linearAnimation: AnimationSpec<Float> = repeatable(
    AnimationConstants.Infinite,
    tween(3000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse
)

val keyframeAnimation: AnimationSpec<Float> = repeatable(
    AnimationConstants.Infinite,
    keyframes {
        durationMillis = 3000
        1f at 0
        1.1f at 1000
        1.6f at 2500
        2f at 3000
    },
    repeatMode = RepeatMode.Reverse
)

val springAnimation: AnimationSpec<Float> = spring(
    dampingRatio = Spring.DampingRatioHighBouncy,
    stiffness = Spring.StiffnessVeryLow,
    visibilityThreshold = 0.001f
)
