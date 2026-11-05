package fr.leboncoin.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/** Identifies the app on every request (API calls and images alike). */
internal class UserAgentInterceptor(private val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request().newBuilder()
            .header("User-Agent", userAgent)
            .build(),
    )
}
