package com.bridge.ouroboros.exampleapplication

import androidx.annotation.StringRes
import com.bridge.ouroboros.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

data class ExampleModel(
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

val ExampleInit: LoopInitializer<ExampleModel, ExampleEffect> =  {
    Next.Change(ExampleModel(loading = true), ExampleEffect.SimulateInitialLoad)
}

sealed class ExampleEvent : ActionableEvent<ExampleModel, ExampleEffect> {
    object LoadCompleted : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(model.copy(loading = false))
    }

    data class UsernameChanged(val value: String) : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(model.copy(username = value))
    }

    data class PasswordChanged(val value: String) : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(model.copy(password = value))
    }

    object LoginClicked : ExampleEvent() {
        override fun perform(model: ExampleModel): Next.Change<ExampleModel, ExampleEffect> {
            val clearedValidation = model.copy(usernameValid = true, passwordValid = true)

            return when {
                model.username.isBlank() -> change(clearedValidation.copy(usernameValid = false))
                model.password.isBlank() -> change(clearedValidation.copy(passwordValid = false))
                else -> change(
                    clearedValidation.copy(loading = true),
                    ExampleEffect.PerformLogin(
                        username = model.username, password = model.password
                    )
                )
            }
        }
    }

    object LoginSucceeded : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(
            model.copy(
                loading = false,
                messages = model.messages + SnackbarMessage(messageRes = R.string.login_succeed)
            )
        )
    }

    object LoginFailed : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(
            model.copy(
                loading = false,
                messages = model.messages + SnackbarMessage(messageRes = R.string.login_failed)
            )
        )
    }

    data class ToastShown(val id: Long) : ExampleEvent() {
        override fun perform(model: ExampleModel) = change(
            model.copy(
                messages = model.messages.filterNot { it.id == id }
            )
        )
    }
}

sealed class ExampleEffect : ExecutableEffect<ExampleEvent, Unit>() {
    object SimulateInitialLoad : ExampleEffect() {
        override fun Unit.perform(emit: EventConsumer<ExampleEvent>) {
            launch {
                delay(3000)
                emit(ExampleEvent.LoadCompleted)
            }
        }
    }

    data class PerformLogin(val username: String, val password: String) : ExampleEffect() {
        override fun Unit.perform(emit: EventConsumer<ExampleEvent>) {
            launch {
                delay(1500)
                emit(ExampleEvent.LoginSucceeded)
            }
        }
    }
}
