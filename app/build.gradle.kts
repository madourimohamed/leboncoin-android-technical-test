plugins {
    alias(libs.plugins.leboncoin.android.application)
    alias(libs.plugins.leboncoin.hilt)
}

android {
    namespace = "fr.leboncoin.androidrecruitmenttestapp"

    defaultConfig {
        applicationId = "fr.leboncoin.androidrecruitmenttestapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    // The app module only assembles: DI graph, navigation, theme.
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.feature.albums)
    implementation(projects.feature.details)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.leakcanary.android)
}
