package com.bridge.ouroboros.compose.test

import com.bridge.ouroboros.compose.EventConsumer

class TestConsumer<T> : EventConsumer<T> {

    val items = mutableSetOf<T>()

    override fun invoke(item: T) {
        items += item
    }

}