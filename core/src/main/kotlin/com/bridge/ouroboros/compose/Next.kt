package com.bridge.ouroboros.compose

sealed class Next<out MODEL, out EFFECT> {
    data class Change<MODEL, EFFECT>(val model: MODEL, override val effects: Set<EFFECT> = emptySet()) : Next<MODEL, EFFECT>() {
        constructor(model: MODEL, vararg effects: EFFECT) : this(model, effects.toSet())

        override fun modelOrNull() = model

        override fun ifHasModel(block: (MODEL) -> Unit) {
            block(model)
        }

        override fun ifHasEffects(block: (Set<EFFECT>) -> Unit) {
            if (effects.isNotEmpty()) {
                block(effects)
            }
        }
    }

    data class Dispatch<EFFECT>(override val effects: Set<EFFECT> = emptySet()) : Next<Nothing, EFFECT>() {
        constructor(vararg effects: EFFECT) : this(effects.toSet())

        override fun ifHasEffects(block: (Set<EFFECT>) -> Unit) {
            if (effects.isNotEmpty()) {
                block(effects)
            }
        }
    }

    object NoChange : Next<Nothing, Nothing>()

    /* default do-nothing */
    open fun ifHasModel(block: (MODEL) -> Unit) {}
    open fun ifHasEffects(block: (Set<EFFECT>) -> Unit) {}

    open fun modelOrNull(): MODEL? = null
    open val effects: Set<EFFECT> = emptySet()
}