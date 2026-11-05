package fr.leboncoin.core.domain.model

/**
 * An entry of the catalogue.
 *
 * @property id unique identifier of the entry (the "track").
 * @property albumId identifier of the album the entry belongs to; shared by several entries.
 */
data class Album(
    val id: Int,
    val albumId: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val isFavorite: Boolean,
)
