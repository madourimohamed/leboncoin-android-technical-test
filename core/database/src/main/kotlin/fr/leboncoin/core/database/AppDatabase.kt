package fr.leboncoin.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.leboncoin.core.database.dao.AlbumDao
import fr.leboncoin.core.database.dao.FavoriteDao
import fr.leboncoin.core.database.model.AlbumEntity
import fr.leboncoin.core.database.model.FavoriteEntity

@Database(
    entities = [AlbumEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun favoriteDao(): FavoriteDao
}
