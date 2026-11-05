package fr.leboncoin.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/** Shared Android + Kotlin configuration for every Android module (app and libraries). */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.version("compileSdk").toInt()

        defaultConfig {
            minSdk = libs.version("minSdk").toInt()
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        testOptions {
            unitTests {
                // Required by Robolectric to load merged resources and manifests.
                isIncludeAndroidResources = true
            }
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        compilerOptions { configureKotlin() }
    }
}

/** Configuration for pure Kotlin/JVM modules (no Android dependency). */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions { configureKotlin() }
    }
}

private fun KotlinJvmCompilerOptions.configureKotlin() {
    jvmTarget.set(JvmTarget.JVM_17)
    freeCompilerArgs.addAll(
        // Make Kotlin interface default methods real JVM default methods.
        "-Xjvm-default=all",
    )
}
