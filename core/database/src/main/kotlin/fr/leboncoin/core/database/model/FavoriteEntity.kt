package fr.leboncoin.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User data, stored apart from the album cache so that replacing the cache never
 * loses favorites. No foreign key on purpose: a favorite whose album temporarily
 * disappears from the remote payload comes back with it.
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey @ColumnInfo(name = "item_id") val itemId: Int,
)
