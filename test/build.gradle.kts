/*
 * Copyright (C) 2019 - present Instructure, Inc.
 */

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "com.bridge.ouroboros.compose.test"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "ouroboroscomposetest.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static(projectDirPath)
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core"))

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.coroutines.core)
            implementation(libs.coroutines.test)
            implementation(libs.junit)
            implementation(libs.kotlin.test)
        }
    }
}
