package com.bridge.ouroboros.exampleapplication

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.bridge.ouroboros.exampleapplication.examples.dictionary.DictionaryExample

data class Example(
    val route: String,
    val icon: ImageVector,
    @StringRes val title: Int,
    val composable: @Composable () -> Unit
)

val knownExamples = listOf(
    Example(
        route = "login",
        icon = Icons.AutoMirrored.Filled.Login,
        title = R.string.login,
        composable = { LoginExample() }
    ),
    Example(
        route = "dictionary",
        icon = Icons.AutoMirrored.Filled.Note,
        title = R.string.dictionary,
        composable = { DictionaryExample() }
    )
)
