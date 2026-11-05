plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "fr.leboncoin.data"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlin.serialization.json)
}
