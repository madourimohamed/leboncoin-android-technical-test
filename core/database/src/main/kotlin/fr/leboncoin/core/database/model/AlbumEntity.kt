package fr.leboncoin.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Cached copy of the remote albums. Entirely replaced on each successful refresh. */
@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "album_id") val albumId: Int,
    val title: String,
    val url: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
)
