plugins {
    id("maven-publish")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.git.version) apply true
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false

}

allprojects {
    group = "com.bridge.ouroboros.compose"
    val gitVersion: groovy.lang.Closure<String>? by extra
    version = gitVersion?.invoke() ?: "SNAPSHOT"
}

subprojects {
    apply(plugin = "maven-publish")

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri(
                    project.findProperty("gpr.repository")?.toString()
                        ?: System.getenv("PACKAGES_REPOSITORY")
                        ?: ""
                )
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
