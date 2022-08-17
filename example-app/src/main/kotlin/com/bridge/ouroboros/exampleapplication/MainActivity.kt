/*
 * Copyright (C) 2019 - present Instructure, Inc.
 */

package com.bridge.ouroboros.exampleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bridge.ouroboros.exampleapplication.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KitchenSinkApp()
        }
    }
}

@Composable
fun KitchenSinkApp() {
    AppTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val navHierarchy = navBackStackEntry?.destination?.hierarchy ?: emptySequence()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (scaffoldState.drawerState.isClosed) {
                                    scaffoldState.drawerState.open()
                                } else {
                                    scaffoldState.drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            drawerContent = {
                DrawerContent(
                    currentHierarchy = navHierarchy,
                    onExampleClicked = { example ->
                        navController.navigate(example.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }

                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            ) {
                CompositionLocalProvider(LocalSnackbarHostState provides scaffoldState.snackbarHostState) {
                    NavHost(navController = navController, startDestination = knownExamples.first().route) {
                        knownExamples.forEach { example ->
                            composable(example.route) {
                                example.composable()
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalSnackbarHostState = compositionLocalOf { SnackbarHostState() }
