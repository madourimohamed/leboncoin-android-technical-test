package fr.leboncoin.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply(libs.pluginId("kotlin-compose"))

    commonExtension.buildFeatures.compose = true

    dependencies {
        val bom = libs.library("androidx-compose-bom")
        "implementation"(platform(bom))
        "testImplementation"(platform(bom))
        "implementation"(libs.library("androidx-ui-tooling-preview"))
        "debugImplementation"(libs.library("androidx-ui-tooling"))
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        // Domain models live in non-Compose modules: declare them stable so that
        // composables receiving them can be skipped during recomposition.
        stabilityConfigurationFiles.add(
            rootProject.layout.projectDirectory.file("compose_stability.conf"),
        )
    }
}
