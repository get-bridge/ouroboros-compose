package com.bridge.ouroboros.exampleapplication

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.runWith
import com.bridge.ouroboros.exampleapplication.examples.login.LoginService
import io.kotest.matchers.collections.shouldContain
import org.junit.Test
import java.io.IOException

class LoginEffectTest {

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
        LoginEffect.SimulateInitialLoad runWith LoginEffect.State() matches {
            emittedEvents shouldContain LoginEvent.LoadCompleted
        }
    }

    @Test
    fun `Perform Login will emit Login Succeeded on login success`() {
        LoginEffect.PerformLogin(
            username = "username",
            password = "password"
        ) runWith LoginEffect.State(SucceedingLoginService) matches {
            emittedEvents shouldContain LoginEvent.LoginSucceeded
        }
    }

    @Test
    fun `Perform Login will emit Login Failed on login failure`() {
        LoginEffect.PerformLogin(
            username = "username",
            password = "password"
        ) runWith LoginEffect.State(FailingLoginService) matches {
            emittedEvents shouldContain LoginEvent.LoginFailed
        }
    }
}