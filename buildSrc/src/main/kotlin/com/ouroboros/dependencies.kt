package com.ouroboros

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.0"

    const val junit = "junit:junit:4.13"

    const val material = "com.google.android.material:material:1.4.0"

    object Kotlin {
        const val version = "1.6.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        const val core = "androidx.core:core:1.6.0"
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val activityKtx = "androidx.activity:activity-ktx:1.3.1"
        const val activityCompose = "androidx.activity:activity-compose:1.3.1"
        const val fragment = "androidx.fragment:fragment:1.3.6"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.6"
        const val navigationUi = "androidx.navigation:navigation-ui:2.3.5"

        object Compose {
            const val version = "1.1.0"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val liveDataRuntime = "androidx.compose.runtime:runtime-livedata:$version"
            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:${version}"

            const val ui = "androidx.compose.ui:ui:${version}"
            const val uiTooling = "androidx.compose.ui:ui-tooling:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${version}"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        }

        object Lifecycle {
            private const val version = "2.3.1"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0"
        }
    }

    object Accompanist {
        private const val version = "0.23.1"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
    }
}
