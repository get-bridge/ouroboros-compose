package com.bridge.ouroboros.compose.test

import com.bridge.ouroboros.compose.Next
import kotlin.test.junit.JUnitAsserter.assertEquals
import kotlin.test.junit.JUnitAsserter.assertNull
import kotlin.test.junit.JUnitAsserter.assertTrue

class NextMatching<M, F>(private val next: Next<M, F>) {

    private var modelAccessed = false

    private var effectsAccessed = false

    val newModel: M?
        get() {
            modelAccessed = true
            return next.modelOrNull()
        }

    val newEffects: Set<F>
        get() {
            effectsAccessed = true
            return next.effects
        }

    val mustHaveModel: M get() {
        modelAccessed = true
        return checkNotNull(next.modelOrNull()) { "Expected model but there was none" }
    }

    fun shouldHaveModel(model: M) {
        assertEquals("Expect new model to be $model", model, newModel)
    }

    fun shouldNotHaveModel() {
        assertNull("Expected no new model", newModel)
    }

    fun shouldHaveEffects(vararg effects: F) {
        newEffects shouldEmit effects.toSet()
    }

    infix fun Set<F>.shouldEmit(effects: Set<F>) {
        assertEquals("Expecting effects to be $effects", effects, newEffects)
    }

    infix fun Set<F>.shouldEmit(effect: F) {
        shouldEmit(setOf(effect))
    }

    fun shouldNotHaveEffects() {
        assertTrue("No effects should be emitted", newEffects.isEmpty())
    }

    fun shouldNotChange() {
        shouldNotHaveModel()
        shouldNotHaveEffects()
    }

    operator fun invoke(block: NextMatching<M, F>.() -> Unit) {
        block(this)
        conclude()
    }

    private fun conclude() {
        if (!modelAccessed) {
            shouldNotHaveModel()
        }

        if (!effectsAccessed) {
            shouldNotHaveEffects()
        }
    }
}
