/*
 * Copyright (C) 2019 - present Instructure, Inc.
 */

package ouroborosexampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bridge.ouroboros.compose.*
import com.example.ouroborosexampleapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            replace(R.id.root, ExamplePureFragment())
        }
    }
}

data class ExampleModel(
    val username: String = "",
    val password: String = "",
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
        override fun perform(model: ExampleModel) = change(
            model.copy(loading = true),
            ExampleEffect.PerformLogin(
                username = model.username, password = model.password
            )
        )
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

class ExamplePureFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val loop = acquireLoop(
                    loopInitializer = ExampleInit,
                    effectStateFactory = { }
                )

                ExampleInterface(model = loop.model, dispatchEvent = loop::dispatchEvent)
            }
        }
    }
}

@Composable
fun ExampleInterface(model: ExampleModel, dispatchEvent: EventConsumer<ExampleEvent>) {
    Column {
        Text(text = stringResource(id = R.string.welcome, model.username))

        Text(text = stringResource(id = R.string.username))

        if (model.loading) {
            CircularProgressIndicator()
        }

        TextField(
            enabled = !model.loading,
            value = model.username,
            onValueChange = { value -> dispatchEvent(ExampleEvent.UsernameChanged(value)) }
        )

        Text(text = stringResource(id = R.string.password))

        TextField(
            enabled = !model.loading,
            value = model.password,
            onValueChange = { value -> dispatchEvent(ExampleEvent.PasswordChanged(value)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(enabled = !model.loading, onClick = { dispatchEvent(ExampleEvent.LoginClicked) }) {
            Text(text = stringResource(id = R.string.login))
        }

        val snackbarState = remember { SnackbarHostState() }

        if (model.messages.isNotEmpty()) {
            val message = model.messages.first()
            val messageText = stringResource(id = message.messageRes)
            LaunchedEffect(message) {
                snackbarState.showSnackbar(messageText)
                dispatchEvent(ExampleEvent.ToastShown(message.id))
            }
        }

        SnackbarHost(hostState = snackbarState)
    }
}

