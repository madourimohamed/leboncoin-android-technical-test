package fr.leboncoin.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Wire representation of an album entry. Kept separate from the domain model on purpose. */
@Serializable
data class NetworkAlbum(
    @SerialName("id") val id: Int,
    @SerialName("albumId") val albumId: Int,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
)
