package fr.leboncoin.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded

/** An album joined with its favorite status. */
data class PopulatedAlbum(
    @Embedded val album: AlbumEntity,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
)
