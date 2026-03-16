package com.bridge.ouroboros.compose.test

data class EffectResult<E>(
    val events: Set<E>
)