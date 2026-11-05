package fr.leboncoin.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.core.data.repository.OfflineFirstAlbumRepository
import fr.leboncoin.core.domain.repository.AlbumRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    @Singleton
    fun bindsAlbumRepository(impl: OfflineFirstAlbumRepository): AlbumRepository
}
