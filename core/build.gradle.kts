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
        namespace = "com.bridge.ouroboros.compose"
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
                outputFileName = "ouroboroscompose.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static(projectDirPath)
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines.core)
            api(libs.jetbrains.compose.runtime)
            api(libs.common.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
