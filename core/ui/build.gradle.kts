plugins {
    alias(libs.plugins.leboncoin.android.library.compose)
}

android {
    namespace = "fr.leboncoin.core.ui"
}

dependencies {
    api(platform(libs.spark.bom))
    api(libs.spark)
    api(libs.androidx.compose.material3)
    api(libs.coil.compose)
}
