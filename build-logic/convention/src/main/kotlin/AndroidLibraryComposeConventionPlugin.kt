import com.android.build.api.dsl.LibraryExtension
import fr.leboncoin.buildlogic.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android library module using Jetpack Compose. */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("leboncoin.android.library")
        extensions.configure<LibraryExtension> {
            configureAndroidCompose(this)
        }
    }
}
