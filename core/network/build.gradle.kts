plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "fr.leboncoin.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://static.leboncoin.fr/\"")
    }
}

dependencies {
    implementation(projects.core.common)

    api(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.coil.network.okhttp)

    testImplementation(libs.okhttp.mockwebserver)
}
