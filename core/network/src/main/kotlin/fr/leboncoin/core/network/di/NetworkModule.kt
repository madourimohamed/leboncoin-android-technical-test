package fr.leboncoin.core.network.di

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.core.network.AlbumApi
import fr.leboncoin.core.network.BuildConfig
import fr.leboncoin.core.network.NetworkJson
import fr.leboncoin.core.network.interceptor.UserAgentInterceptor
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    private const val USER_AGENT = "LeboncoinApp/1.0"
    private const val TIMEOUT_SECONDS = 20L

    /** Base client shared by the API and the image loader (one connection pool, one dispatcher). */
    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(UserAgentInterceptor(USER_AGENT))
        .build()

    @Provides
    @Singleton
    fun providesAlbumApi(okHttpClient: Lazy<OkHttpClient>): AlbumApi {
        // Body logging is only enabled on API calls, never on image downloads.
        val apiClient by lazy {
            okHttpClient.get().newBuilder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
                        )
                    }
                }
                .build()
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            // Defer the OkHttp initialization to the first request, off the main thread.
            .callFactory { request -> apiClient.newCall(request) }
            .addConverterFactory(NetworkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    /**
     * Coil reuses the shared OkHttp client. Its memory and disk caches are enabled by default,
     * so thumbnails already seen stay available offline.
     */
    @Provides
    @Singleton
    fun providesImageLoader(
        @ApplicationContext context: Context,
        okHttpClient: Lazy<OkHttpClient>,
    ): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient.get() as Call.Factory }))
        }
        .crossfade(true)
        .build()
}
