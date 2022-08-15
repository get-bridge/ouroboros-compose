package com.bridge.ouroboros.exampleapplication

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.runWith
import io.kotest.matchers.collections.shouldContain
import org.junit.Test

class ExampleEffectTest {
    @Test
    fun `Simulate Initial Load will emit Load Completed`() {
        ExampleEffect.SimulateInitialLoad runWith Unit matches {
            emittedEvents shouldContain ExampleEvent.LoadCompleted
        }
    }

    @Test
    fun `Perform Login will emit Login Succeeded`() {
        ExampleEffect.PerformLogin(
            username = "username",
            password = "password"
        ) runWith Unit matches {
            emittedEvents shouldContain ExampleEvent.LoginSucceeded
        }
    }
}