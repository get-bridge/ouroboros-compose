package com.bridge.ouroboros.compose

interface ActionableEvent<MODEL, EFFECT> {
    fun perform(model: MODEL): Next<MODEL, EFFECT>

    fun change(model: MODEL, vararg effects: EFFECT) = Next.Change(model, *effects)

    fun dispatch(vararg effects: EFFECT) = Next.Dispatch(*effects)

    companion object {
        val noChange = Next.NoChange
    }
}