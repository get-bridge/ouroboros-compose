package com.bridge.ouroboros.compose.test

import com.bridge.ouroboros.compose.Next
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


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

    val mustHaveModel: M
        get() {
            modelAccessed = true
            return checkNotNull(next.modelOrNull()) { "Expected model but there was none" }
        }

    fun shouldHaveModel(model: M) {
        assertEquals(expected = model, actual = newModel, message = "Expect new model to be $model")
    }

    fun shouldNotHaveModel() {
        assertNull(newModel, "Expected no new model")
    }

    fun shouldHaveEffects(vararg effects: F) {
        newEffects shouldEmit effects.toSet()
    }

    infix fun Set<F>.shouldEmit(effects: Set<F>) {
        assertEquals(
            expected = effects,
            actual = newEffects,
            message = "Expecting effects to be $effects"
        )
    }

    infix fun Set<F>.shouldEmit(effect: F) {
        shouldEmit(setOf(effect))
    }

    fun shouldNotHaveEffects() {
        assertTrue(newEffects.isEmpty(), "No effects should be emitted")
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
