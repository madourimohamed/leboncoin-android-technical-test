plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.hilt)
}

android {
    namespace = "fr.leboncoin.core.common"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}
