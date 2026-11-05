package fr.leboncoin.core.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.core.analytics.AnalyticsHelper
import fr.leboncoin.core.analytics.LogcatAnalyticsHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AnalyticsModule {

    @Binds
    @Singleton
    fun bindsAnalyticsHelper(impl: LogcatAnalyticsHelper): AnalyticsHelper
}
