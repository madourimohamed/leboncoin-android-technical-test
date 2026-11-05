import com.android.build.api.dsl.ApplicationExtension
import fr.leboncoin.buildlogic.configureAndroidCompose
import fr.leboncoin.buildlogic.configureKotlinAndroid
import fr.leboncoin.buildlogic.libs
import fr.leboncoin.buildlogic.pluginId
import fr.leboncoin.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Android application module, Compose enabled. */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("android-application"))
        pluginManager.apply(libs.pluginId("kotlin-android"))

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureAndroidCompose(this)
            defaultConfig.targetSdk = libs.version("targetSdk").toInt()
        }
    }
}
