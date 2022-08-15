/*
 * Copyright (C) 2019 - present Instructure, Inc.
 */

package com.bridge.ouroboros.exampleapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bridge.ouroboros.compose.EventConsumer
import com.bridge.ouroboros.compose.acquireLoop

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val loop = acquireLoop(
                loopInitializer = ExampleInit,
                effectStateFactory = { }
            )

            ExampleScreen(model = loop.model, dispatchEvent = loop::dispatchEvent)
        }
    }
}

@Composable
fun ExampleScreen(model: ExampleModel, dispatchEvent: EventConsumer<ExampleEvent>) {
    Box(modifier = Modifier.fillMaxWidth()) {
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
                onValueChange = { value -> dispatchEvent(ExampleEvent.UsernameChanged(value)) },
                enabled = !model.loading,
                isError = !model.usernameValid,
                singleLine = true
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.password)) },
                value = model.password,
                onValueChange = { value -> dispatchEvent(ExampleEvent.PasswordChanged(value)) },
                enabled = !model.loading,
                visualTransformation = PasswordVisualTransformation(),
                isError = !model.passwordValid,
                singleLine = true
            )

            Button(
                modifier = Modifier.align(Alignment.End),
                enabled = !model.loading,
                onClick = { dispatchEvent(ExampleEvent.LoginClicked) }) {
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

        val snackbarState = remember { SnackbarHostState() }

        if (model.messages.isNotEmpty()) {
            val message = model.messages.first()
            val messageText = stringResource(id = message.messageRes)
            LaunchedEffect(message) {
                snackbarState.showSnackbar(messageText)
                dispatchEvent(ExampleEvent.ToastShown(message.id))
            }
        }

        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarState
        )
    }
}

@Composable
@Preview
fun ExampleScreenPreview() {
    ExampleScreen(
        model = ExampleModel(username = "someuser", password = "somepass"),
        dispatchEvent = {}
    )
}

@Composable
@Preview
fun ExampleScreenLoadingPreview() {
    ExampleScreen(
        model = ExampleModel(username = "someuser", password = "somepass", loading = true),
        dispatchEvent = {}
    )
}