package com.bridge.ouroboros.compose.test

import io.kotest.assertions.withClue
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class EffectMatching<E>(
    capturedEvents: Set<E>
) {
    private var eventsAccessed = false

    val emittedEvents = capturedEvents
        get() {
            eventsAccessed = true
            return field
        }

    fun expectEvents(vararg events: E) {
        withClue("Expected events to be $events") {
            emittedEvents shouldContainExactlyInAnyOrder  events.toSet()
        }
    }

    fun expectNoEvents() {
        withClue("Expected events to be empty") {
            emittedEvents.shouldBeEmpty()
        }
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

fun <E> haveNoEvents() = object : Matcher<EffectResult<E>> {
    override fun test(value: EffectResult<E>) = MatcherResult(
        value.events.isEmpty(),
        { "Effect should not have emitted any events but were ${value.events}" },
        { "Effect should have emitted at least one event but no events were sent" }
    )
}