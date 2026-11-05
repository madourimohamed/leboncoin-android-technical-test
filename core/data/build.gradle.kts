plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.hilt)
}

android {
    namespace = "fr.leboncoin.core.data"
}

dependencies {
    api(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.core.network)

    // Only used to classify failures (HttpException, SerializationException).
    implementation(libs.retrofit.core)
    implementation(libs.kotlin.serialization.json)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.robolectric)
}
