package fr.leboncoin.core.data.model

import fr.leboncoin.core.database.model.AlbumEntity
import fr.leboncoin.core.database.model.PopulatedAlbum
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.network.model.NetworkAlbum

internal fun NetworkAlbum.asEntity() = AlbumEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

internal fun PopulatedAlbum.asExternalModel() = Album(
    id = album.id,
    albumId = album.albumId,
    title = album.title,
    imageUrl = album.url,
    thumbnailUrl = album.thumbnailUrl,
    isFavorite = isFavorite,
)
