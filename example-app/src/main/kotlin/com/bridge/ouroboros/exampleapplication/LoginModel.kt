package com.bridge.ouroboros.exampleapplication

import androidx.annotation.StringRes
import com.bridge.ouroboros.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

data class LoginModel(
    val username: String = "",
    val password: String = "",
    val usernameValid: Boolean = true,
    val passwordValid: Boolean = true,
    val loading: Boolean = false,
    val messages: List<SnackbarMessage> = emptyList()
)

data class SnackbarMessage(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    @StringRes val messageRes: Int
)

val LoginInit: LoopInitializer<LoginModel, LoginEffect> =  {
    Next.Change(LoginModel(loading = true), LoginEffect.SimulateInitialLoad)
}

sealed class LoginEvent : ActionableEvent<LoginModel, LoginEffect> {
    object LoadCompleted : LoginEvent() {
        override fun perform(model: LoginModel) = change(model.copy(loading = false))
    }

    data class UsernameChanged(val value: String) : LoginEvent() {
        override fun perform(model: LoginModel) = change(model.copy(username = value))
    }

    data class PasswordChanged(val value: String) : LoginEvent() {
        override fun perform(model: LoginModel) = change(model.copy(password = value))
    }

    object LoginClicked : LoginEvent() {
        override fun perform(model: LoginModel): Next.Change<LoginModel, LoginEffect> {
            val clearedValidation = model.copy(usernameValid = true, passwordValid = true)

            return when {
                model.username.isBlank() -> change(clearedValidation.copy(usernameValid = false))
                model.password.isBlank() -> change(clearedValidation.copy(passwordValid = false))
                else -> change(
                    clearedValidation.copy(loading = true),
                    LoginEffect.PerformLogin(
                        username = model.username, password = model.password
                    )
                )
            }
        }
    }

    object LoginSucceeded : LoginEvent() {
        override fun perform(model: LoginModel) = change(
            model.copy(
                loading = false,
                messages = model.messages + SnackbarMessage(messageRes = R.string.login_succeed)
            )
        )
    }

    object LoginFailed : LoginEvent() {
        override fun perform(model: LoginModel) = change(
            model.copy(
                loading = false,
                messages = model.messages + SnackbarMessage(messageRes = R.string.login_failed)
            )
        )
    }

    data class ToastShown(val id: Long) : LoginEvent() {
        override fun perform(model: LoginModel) = change(
            model.copy(
                messages = model.messages.filterNot { it.id == id }
            )
        )
    }
}

sealed class LoginEffect : ExecutableEffect<LoginEvent, LoginEffect.State>() {
    object SimulateInitialLoad : LoginEffect() {
        override fun State.perform(emit: EventConsumer<LoginEvent>) {
            launch {
                delay(3000)
                emit(LoginEvent.LoadCompleted)
            }
        }
    }

    data class PerformLogin(val username: String, val password: String) : LoginEffect() {
        override fun State.perform(emit: EventConsumer<LoginEvent>) {
            launch {
                try {
                    loginService.login(username, password)
                    emit(LoginEvent.LoginSucceeded)
                } catch (e: Exception) {
                    emit(LoginEvent.LoginFailed)
                }
            }
        }
    }

    class State(
        val loginService: LoginService = DummyLoginService()
    )
}
