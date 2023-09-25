package com.bridge.ouroboros.compose

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class ExecutableEffect<EVENT, EFFECT_STATE> : CoroutineScope {

    private val job = SupervisorJob()

    private var currentContext: CoroutineContext? = null

    override val coroutineContext: CoroutineContext
        get() {
            checkNotNull(currentContext) { "coroutineContext must be attached before invoking perform" }
            return job + defaultDispatcher
        }

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

    companion object {
        @Volatile
        var defaultDispatcher: CoroutineDispatcher = Dispatchers.Main
            private set

        fun setDefaultDispatcher(dispatcher: CoroutineDispatcher) {
            defaultDispatcher = dispatcher
        }
    }
}
