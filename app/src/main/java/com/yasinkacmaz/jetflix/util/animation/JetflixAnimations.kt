package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

val linearAnimation = InfiniteRepeatableSpec<Float>(
    animation = tween(3000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse,
)

val keyframeAnimation: AnimationSpec<Float> = infiniteRepeatable(
    keyframes {
        durationMillis = 3000
        1f at 0
        1.1f at 1000
        1.6f at 2500
        2f at 3000
    },
    repeatMode = RepeatMode.Reverse,
)

val springAnimation = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow,
    visibilityThreshold = 0.001f,
)

val defaultScaleAnimation = infiniteRepeatable<Float>(
    tween(3000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse,
)
