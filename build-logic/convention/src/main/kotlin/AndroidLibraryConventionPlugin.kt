import com.android.build.api.dsl.LibraryExtension
import fr.leboncoin.buildlogic.configureKotlinAndroid
import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.library
import fr.leboncoin.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/** Android library module with the shared unit-test toolbox. */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-library"))
        pluginManager.apply(libs.pluginId("kotlin-android"))

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this)
            defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            defaultConfig.consumerProguardFiles("consumer-rules.pro")
        }

        dependencies {
            "testImplementation"(libs.library("junit"))
            "testImplementation"(libs.library("kotlinx-coroutines-test"))
            "testImplementation"(libs.library("turbine"))
        }
    }
}
