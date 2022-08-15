package com.bridge.ouroboros.compose

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class ExecutableEffect<EVENT, EFFECT_STATE> : CoroutineScope {

    private var currentContext: CoroutineContext? = null

    override val coroutineContext: CoroutineContext get() = checkNotNull(currentContext) { "coroutineContext must be attached before invoking perform" }

    abstract fun EFFECT_STATE.perform(emit: EventConsumer<EVENT>)

    fun runInContext(
        coroutineContext: CoroutineContext,
        state: EFFECT_STATE,
        emit: EventConsumer<EVENT>
    ) {
        try {
            currentContext = coroutineContext
            state.perform(emit)
        } finally {
            currentContext = null
        }
    }
}