package com.bridge.ouroboros.exampleapplication

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.receives
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.Test

class ExampleEventTest {
    @Test
    fun `Load Completed clears loading flag`() {
        ExampleModel(loading = true) receives ExampleEvent.LoadCompleted matches {
            mustHaveModel.loading shouldBe false
        }
    }

    @Test
    fun `Username Changed will update username`() {
        val value = "newusername"
        ExampleModel(username = "oldusername") receives ExampleEvent.UsernameChanged(value) matches {
            mustHaveModel.username shouldBe value
        }
    }

    @Test
    fun `Password Changed will update password`() {
        val value = "newpassword"
        ExampleModel(password = value) receives ExampleEvent.PasswordChanged(value) matches {
            mustHaveModel.password shouldBe value
        }
    }

    @Test
    fun `Login Clicked will fail to validate with blank username`() {
        ExampleModel(username = "") receives ExampleEvent.LoginClicked matches {
            mustHaveModel.usernameValid shouldBe false
        }
    }

    @Test
    fun `Login Clicked will fail to validate with blank password`() {
        ExampleModel(username = "something", password = "") receives ExampleEvent.LoginClicked matches {
            mustHaveModel.passwordValid shouldBe false
        }
    }

    @Test
    fun `Login Clicked will proceed with non-empty username and password`() {
        val username = "someuser"
        val password = "somepass"

        ExampleModel(
            username = username,
            password = password
        ) receives ExampleEvent.LoginClicked matches {
            mustHaveModel.loading shouldBe true
            newEffects shouldContain ExampleEffect.PerformLogin(
                username = username,
                password = password
            )
        }
    }

    @Test
    fun `Login Succeeded will clear loading flag and show snackbar message`() {
        ExampleModel(loading = true) receives ExampleEvent.LoginSucceeded matches {
            mustHaveModel.loading shouldBe false
            mustHaveModel.messages.shouldHaveSingleElement {
                it.messageRes == R.string.login_succeed
            }
        }
    }

    @Test
    fun `Login Failed will clear loading flag and show snackbar message`() {
        ExampleModel(loading = true) receives ExampleEvent.LoginFailed matches {
            mustHaveModel.loading shouldBe false
            mustHaveModel.messages.shouldHaveSingleElement {
                it.messageRes == R.string.login_failed
            }
        }
    }
}