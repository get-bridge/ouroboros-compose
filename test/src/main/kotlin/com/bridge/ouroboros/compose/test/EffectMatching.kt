package com.bridge.ouroboros.compose.test

import kotlin.test.junit.JUnitAsserter.assertEquals
import kotlin.test.junit.JUnitAsserter.assertTrue

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
        assertEquals("Expected events to be $events", events.toSet(), emittedEvents.toSet())
    }

    fun expectNoEvents() {
        assertTrue("Expected events to be empty", emittedEvents.isEmpty())
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
