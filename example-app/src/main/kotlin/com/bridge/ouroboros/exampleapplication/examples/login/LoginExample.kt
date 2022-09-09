package com.bridge.ouroboros.exampleapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bridge.ouroboros.compose.EventConsumer
import com.bridge.ouroboros.compose.acquireLoop
import com.bridge.ouroboros.exampleapplication.theme.AppTheme

@Composable
fun LoginExample() {
    val loop = acquireLoop(
        loopInitializer = LoginInit,
        effectStateFactory = LoginEffect::State
    )

    LoginScreen(model = loop.model, dispatchEvent = loop::dispatchEvent)
}

@Composable
fun LoginScreen(model: LoginModel, dispatchEvent: EventConsumer<LoginEvent>) {
    if (model.messages.isNotEmpty()) {
        val message = model.messages.first()
        val messageText = stringResource(id = message.messageRes)

        val snackbarHostState = LocalSnackbarHostState.current
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(messageText)
            dispatchEvent(LoginEvent.ToastShown(message.id))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (model.username.isBlank()) {
                stringResource(id = R.string.enter_your_credentials)
            } else {
                stringResource(id = R.string.welcome, model.username)
            },
            style = MaterialTheme.typography.h6
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.username)) },
            value = model.username,
            onValueChange = { value -> dispatchEvent(LoginEvent.UsernameChanged(value)) },
            enabled = !model.loading,
            isError = !model.usernameValid,
            singleLine = true
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.password)) },
            value = model.password,
            onValueChange = { value -> dispatchEvent(LoginEvent.PasswordChanged(value)) },
            enabled = !model.loading,
            visualTransformation = PasswordVisualTransformation(),
            isError = !model.passwordValid,
            singleLine = true
        )

        Button(
            modifier = Modifier.align(Alignment.End),
            enabled = !model.loading,
            onClick = { dispatchEvent(LoginEvent.LoginClicked) }) {
            Text(text = stringResource(id = R.string.login))
        }
    }

    if (model.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity))
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 60.dp),
                elevation = 4.dp
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
@Preview(group = "Login example")
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            model = LoginModel(username = "someuser", password = "somepass"),
            dispatchEvent = {}
        )
    }
}

@Composable
@Preview(group = "Login example")
fun LoginScreenLoadingPreview() {
    AppTheme {
        LoginScreen(
            model = LoginModel(username = "someuser", password = "somepass", loading = true),
            dispatchEvent = {}
        )
    }
}
