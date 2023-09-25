package com.bridge.ouroboros.compose.test

import com.bridge.ouroboros.compose.ActionableEvent
import com.bridge.ouroboros.compose.ExecutableEffect
import com.bridge.ouroboros.compose.Next
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

infix fun <M, E : ActionableEvent<M, F>, F> M.receives(event: E) =
    event.perform(this)

infix fun <F : ExecutableEffect<E, S>, E, S> F.runWith(state: S): EffectResult<E> {
    val eventConsumer = TestConsumer<E>()

    runBlocking {
        runInContext(coroutineContext, state, eventConsumer)
    }

    return EffectResult(eventConsumer.items)
}

fun <F : ExecutableEffect<E, S>, E, S> F.runWith(
    state: S,
    context: CoroutineContext
): EffectResult<E> {
    val eventConsumer = TestConsumer<E>()

    runInContext(context, state, eventConsumer)

    return EffectResult(eventConsumer.items)
}

infix fun <M, F> Next<M, F>.matches(block: NextMatching<M, F>.() -> Unit) =
    NextMatching(this)(block)

infix fun <E> EffectResult<E>.matches(block: EffectMatching<E>.() -> Unit) {
    EffectMatching(events)(block)
}
