package fr.leboncoin.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.core.database.AppDatabase
import fr.leboncoin.core.database.dao.AlbumDao
import fr.leboncoin.core.database.dao.FavoriteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "leboncoin.db").build()

    @Provides
    fun providesAlbumDao(database: AppDatabase): AlbumDao = database.albumDao()

    @Provides
    fun providesFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()
}
