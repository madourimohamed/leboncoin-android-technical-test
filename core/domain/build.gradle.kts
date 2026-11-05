plugins {
    alias(libs.plugins.leboncoin.jvm.library)
}

dependencies {
    // paging-common is a pure Kotlin artifact: PagingData can be part of the contract
    // without pulling the Android framework into the domain.
    api(libs.androidx.paging.common)
    api(libs.kotlinx.coroutines.core)
}
