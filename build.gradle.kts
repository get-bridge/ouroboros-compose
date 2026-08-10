import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec

plugins {
    id("maven-publish")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.git.version) apply true
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false

}

allprojects {
    group = "com.bridge.ouroboros.compose"
    version = "1.0.4"
}

subprojects {
    apply(plugin = "maven-publish")

    publishing {
        repositories {
            val repositoryUrl = project.findProperty("gpr.repository")?.toString()
                ?: System.getenv("PACKAGES_REPOSITORY")
            if (!repositoryUrl.isNullOrBlank()) {
                maven {
                    name = "GitHubPackages"
                    url = uri(repositoryUrl)
                    credentials {
                        username = project.findProperty("gpr.user")?.toString()
                            ?: System.getenv("PACKAGES_USERNAME")
                        password = project.findProperty("gpr.key")?.toString()
                            ?: System.getenv("PACKAGES_TOKEN")
                    }
                }
            }
        }
    }
}

rootProject.plugins.withType<YarnPlugin> {
    rootProject.extensions.configure<YarnRootEnvSpec> {
        version.set("1.22.19")
    }
}
