rootProject.name = "ouroboros-compose"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven { url = java.net.URI.create("https://jitpack.io") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        exclusiveContent {
            forRepository {
                ivy("https://nodejs.org/dist/") {
                    name = "Node Distributions at $url"
                    patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
                    metadataSources { artifact() }
                    content { includeModule("org.nodejs", "node") }
                }
            }
            filter { includeGroup("org.nodejs") }
        }
        exclusiveContent {
            forRepository {
                ivy("https://github.com/yarnpkg/yarn/releases/download") {
                    name = "Yarn Distributions at $url"
                    patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
                    metadataSources { artifact() }
                    content { includeModule("com.yarnpkg", "yarn") }
                }
            }
            filter { includeGroup("com.yarnpkg") }
        }
        exclusiveContent {
            forRepository {
                ivy("https://github.com/WebAssembly/binaryen/releases/download") {
                    name = "Binaryen Distributions at $url"
                    patternLayout { artifact("version_[revision]/[module]-version_[revision]-[classifier].[ext]") }
                    metadataSources { artifact() }
                    content { includeModule("com.github.webassembly", "binaryen") }
                }
            }
            filter { includeGroup("com.github.webassembly") }
        }
    }
}

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":core",
    ":example-app",
    ":test",
)
