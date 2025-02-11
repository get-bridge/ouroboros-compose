package com.bridge.ouroboros.compose.test

import kotlin.test.assertEquals
import kotlin.test.assertTrue


class EffectMatching<E>(
    capturedEvents: Set<E>,
) {
    private var eventsAccessed = false

    val emittedEvents = capturedEvents
        get() {
            eventsAccessed = true
            return field
        }

    fun expectEvents(vararg events: E) {
        assertEquals(events.toSet(), emittedEvents.toSet(),"Expected events to be $events")
    }

    fun expectNoEvents() {
        assertTrue(emittedEvents.isEmpty(), "Expected events to be empty")
    }

    operator fun invoke(block: EffectMatching<E>.() -> Unit) {
        block(this)
        conclude()
    }

    private fun conclude() {
        if (!eventsAccessed) {
            expectNoEvents()
        }
    }
}
