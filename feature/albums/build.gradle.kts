plugins {
    alias(libs.plugins.leboncoin.android.feature)
}

android {
    namespace = "fr.leboncoin.feature.albums"
}

dependencies {
    implementation(libs.androidx.paging.compose)
}
