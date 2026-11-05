plugins {
    alias(libs.plugins.leboncoin.android.library)
}

android {
    namespace = "fr.leboncoin.core.testing"
}

dependencies {
    api(projects.core.domain)
    api(projects.core.analytics)

    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
    api(libs.androidx.paging.testing)
}
