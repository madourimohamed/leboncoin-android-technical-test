import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.library
import fr.leboncoin.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Hilt with KSP (faster than kapt). */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("ksp"))
        pluginManager.apply(libs.pluginId("hilt"))

        dependencies {
            "implementation"(libs.library("hilt-android"))
            "ksp"(libs.library("hilt-compiler"))
        }
    }
}
