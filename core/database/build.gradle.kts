plugins {
    alias(libs.plugins.leboncoin.android.library)
    alias(libs.plugins.leboncoin.android.room)
    alias(libs.plugins.leboncoin.hilt)
}

android {
    namespace = "fr.leboncoin.core.database"
}

dependencies {
    api(libs.room.paging)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.robolectric)
}
