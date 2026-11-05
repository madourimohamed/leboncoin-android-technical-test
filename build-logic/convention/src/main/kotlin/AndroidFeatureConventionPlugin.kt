import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.library
import fr.leboncoin.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Feature module: a self-contained screen (UI + ViewModel + navigation entry point).
 * Features only depend on core modules, never on each other.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("leboncoin.android.library.compose")
        pluginManager.apply("leboncoin.hilt")
        // Type-safe navigation routes are @Serializable classes.
        pluginManager.apply(libs.pluginId("kotlin-serialization"))

        dependencies {
            "implementation"(project(":core:domain"))
            "implementation"(project(":core:ui"))
            "implementation"(project(":core:analytics"))

            "implementation"(libs.library("androidx-hilt-navigation-compose"))
            "implementation"(libs.library("androidx-lifecycle-runtime-compose"))
            "implementation"(libs.library("androidx-lifecycle-viewmodel-compose"))
            "implementation"(libs.library("androidx-navigation-compose"))
            "implementation"(libs.library("kotlin-serialization-json"))

            "testImplementation"(project(":core:testing"))
            "testImplementation"(libs.library("androidx-ui-test-junit4"))
            "testImplementation"(libs.library("robolectric"))
            "debugImplementation"(libs.library("androidx-ui-test-manifest"))
        }
    }
}
