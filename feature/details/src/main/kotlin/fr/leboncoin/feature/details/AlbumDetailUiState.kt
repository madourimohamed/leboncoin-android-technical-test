package fr.leboncoin.feature.details

import fr.leboncoin.core.domain.model.Album

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val album: Album) : AlbumDetailUiState

    /** The album is not in the cache, e.g. it was removed by the last refresh. */
    data object NotFound : AlbumDetailUiState
}
