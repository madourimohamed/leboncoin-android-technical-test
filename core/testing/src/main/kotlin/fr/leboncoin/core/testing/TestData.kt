package fr.leboncoin.core.testing

import fr.leboncoin.core.domain.model.Album

fun testAlbum(id: Int, isFavorite: Boolean = false) = Album(
    id = id,
    albumId = 1 + id / 50,
    title = "Album title $id",
    imageUrl = "https://placehold.co/600x600/$id",
    thumbnailUrl = "https://placehold.co/150x150/$id",
    isFavorite = isFavorite,
)
