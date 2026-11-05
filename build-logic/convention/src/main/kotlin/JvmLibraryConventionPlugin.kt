import fr.leboncoin.buildlogic.configureKotlinJvm
import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.library
import fr.leboncoin.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Pure Kotlin module: no Android framework, fastest to build and test. */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-jvm"))
        configureKotlinJvm()

        dependencies {
            "testImplementation"(libs.library("junit"))
        }
    }
}
