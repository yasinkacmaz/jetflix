package com.yasinkacmaz.jetflix.util.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

val springAnimation = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow,
    visibilityThreshold = 0.001f,
)

enum class AnimationDuration(val duration: Int) {
    SHORT(400),
    MEDIUM(800),
    LONG(1200),
}
