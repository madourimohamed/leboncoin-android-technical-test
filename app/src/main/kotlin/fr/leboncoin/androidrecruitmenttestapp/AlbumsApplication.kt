package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class AlbumsApplication : Application(), SingletonImageLoader.Factory {

    /** Lazy: the image loader (and its OkHttp client) is only built when the first image loads. */
    @Inject
    lateinit var imageLoader: Lazy<ImageLoader>

    override fun newImageLoader(context: PlatformContext): ImageLoader = imageLoader.get()
}
