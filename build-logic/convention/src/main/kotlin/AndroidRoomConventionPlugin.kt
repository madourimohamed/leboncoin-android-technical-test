import androidx.room.gradle.RoomExtension
import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.library
import fr.leboncoin.buildlogic.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/** Room with KSP; schemas are exported and versioned to validate future migrations. */
class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("room"))
        pluginManager.apply(libs.pluginId("ksp"))

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            "implementation"(libs.library("room-runtime"))
            "implementation"(libs.library("room-ktx"))
            "ksp"(libs.library("room-compiler"))
        }
    }
}
