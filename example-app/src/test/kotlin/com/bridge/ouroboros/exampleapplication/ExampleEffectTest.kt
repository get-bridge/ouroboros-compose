package com.bridge.ouroboros.exampleapplication

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.runWith
import io.kotest.matchers.collections.shouldContain
import org.junit.Test
import java.io.IOException

class ExampleEffectTest {

    object SucceedingLoginService : LoginService {
        override suspend fun login(username: String, password: String) {}
    }

    object FailingLoginService : LoginService {
        override suspend fun login(username: String, password: String) {
            throw IOException("Oh my!")
        }
    }

    @Test
    fun `Simulate Initial Load will emit Load Completed`() {
        ExampleEffect.SimulateInitialLoad runWith ExampleEffect.State() matches {
            emittedEvents shouldContain ExampleEvent.LoadCompleted
        }
    }

    @Test
    fun `Perform Login will emit Login Succeeded on login success`() {
        ExampleEffect.PerformLogin(
            username = "username",
            password = "password"
        ) runWith ExampleEffect.State(SucceedingLoginService) matches {
            emittedEvents shouldContain ExampleEvent.LoginSucceeded
        }
    }

    @Test
    fun `Perform Login will emit Login Failed on login failure`() {
        ExampleEffect.PerformLogin(
            username = "username",
            password = "password"
        ) runWith ExampleEffect.State(FailingLoginService) matches {
            emittedEvents shouldContain ExampleEvent.LoginFailed
        }
    }
}