package com.bridge.ouroboros.exampleapplication

import com.bridge.ouroboros.compose.test.matches
import com.bridge.ouroboros.compose.test.receives
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.Test

class LoginEventTest {
    @Test
    fun `Load Completed clears loading flag`() {
        LoginModel(loading = true) receives LoginEvent.LoadCompleted matches {
            mustHaveModel.loading shouldBe false
        }
    }

    @Test
    fun `Username Changed will update username`() {
        val value = "newusername"
        LoginModel(username = "oldusername") receives LoginEvent.UsernameChanged(value) matches {
            mustHaveModel.username shouldBe value
        }
    }

    @Test
    fun `Password Changed will update password`() {
        val value = "newpassword"
        LoginModel(password = value) receives LoginEvent.PasswordChanged(value) matches {
            mustHaveModel.password shouldBe value
        }
    }

    @Test
    fun `Login Clicked will fail to validate with blank username`() {
        LoginModel(username = "") receives LoginEvent.LoginClicked matches {
            mustHaveModel.usernameValid shouldBe false
        }
    }

    @Test
    fun `Login Clicked will fail to validate with blank password`() {
        LoginModel(username = "something", password = "") receives LoginEvent.LoginClicked matches {
            mustHaveModel.passwordValid shouldBe false
        }
    }

    @Test
    fun `Login Clicked will proceed with non-empty username and password`() {
        val username = "someuser"
        val password = "somepass"

        LoginModel(
            username = username,
            password = password
        ) receives LoginEvent.LoginClicked matches {
            mustHaveModel.loading shouldBe true
            newEffects shouldContain LoginEffect.PerformLogin(
                username = username,
                password = password
            )
        }
    }

    @Test
    fun `Login Succeeded will clear loading flag and show snackbar message`() {
        LoginModel(loading = true) receives LoginEvent.LoginSucceeded matches {
            mustHaveModel.loading shouldBe false
            mustHaveModel.messages.shouldHaveSingleElement {
                it.messageRes == R.string.login_succeed
            }
        }
    }

    @Test
    fun `Login Failed will clear loading flag and show snackbar message`() {
        LoginModel(loading = true) receives LoginEvent.LoginFailed matches {
            mustHaveModel.loading shouldBe false
            mustHaveModel.messages.shouldHaveSingleElement {
                it.messageRes == R.string.login_failed
            }
        }
    }
}