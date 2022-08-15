package com.bridge.ouroboros.compose.test

import com.bridge.ouroboros.compose.Next
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

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

    fun shouldHaveModel(model: M) {
        withClue("Expect new model to be $model") {
            newModel shouldBe model
        }
    }

    fun shouldNotHaveModel() {
        withClue("Expected no new model") {
            newModel shouldBe null
        }
    }

    fun shouldHaveEffects(vararg effects: F) {
        newEffects shouldEmit effects.toSet()
    }

    infix fun Set<F>.shouldEmit(effects: Set<F>) {
        withClue("Expecting effects to be $effects") {
            newEffects shouldContainExactlyInAnyOrder effects
        }
    }

    infix fun Set<F>.shouldEmit(effect: F) {
        shouldEmit(setOf(effect))
    }

    fun shouldNotHaveEffects() {
        withClue("No effects should be emitted") {
            newEffects.shouldBeEmpty()
        }
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
