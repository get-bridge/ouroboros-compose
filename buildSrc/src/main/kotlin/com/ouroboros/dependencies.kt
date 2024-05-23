package com.ouroboros

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:8.4.1"

    const val junit = "junit:junit:4.13"

    const val material = "com.google.android.material:material:1.4.0"

    object Kotlin {
        private const val version = "1.9.22"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val testJUnit = "org.jetbrains.kotlin:kotlin-test-junit:$version"
    }

    object Coroutines {
        private const val version = "1.7.3"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Ktor {
        private const val version = "2.1.0"
        const val core = "io.ktor:ktor-client-core:$version"
        const val android = "io.ktor:ktor-client-android:$version"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:$version"
    }

    object Kotest {
        private const val version = "5.7.2"
        const val assertions = "io.kotest:kotest-assertions-core:$version"
    }

    object MockK {
        private const val version = "1.13.10"
        const val library = "io.mockk:mockk:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.6.1"

        const val activityCompose = "androidx.activity:activity-compose:1.8.2"
        const val navigationCompose = "androidx.navigation:navigation-compose:2.5.3"

        object Compose {
            const val bomVersion = "2024.05.00"
            const val version = "1.5.1"
            const val compiler = "1.5.9"

            const val bom = "androidx.compose:compose-bom:$bomVersion"
            const val runtime = "androidx.compose.runtime:runtime"
            const val foundation = "androidx.compose.foundation:foundation"
            const val ui = "androidx.compose.ui:ui"
            const val uiTooling = "androidx.compose.ui:ui-tooling"
            const val material = "androidx.compose.material:material"
            const val materialIconsExtended =
                "androidx.compose.material:material-icons-extended"
        }

        object Test {
            private const val version = "1.5.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.5"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
        }

        object Lifecycle {
            private const val version = "2.7.0"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }
    }
}
