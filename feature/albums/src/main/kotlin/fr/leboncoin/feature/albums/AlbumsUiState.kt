package fr.leboncoin.feature.albums

import fr.leboncoin.core.domain.model.DataError

/**
 * Screen state besides the paged albums themselves, which are exposed as a separate
 * Paging flow.
 *
 * @property error last synchronization failure, cleared once displayed or on the next refresh.
 */
data class AlbumsUiState(
    val isRefreshing: Boolean = false,
    val favoritesOnly: Boolean = false,
    val error: DataError? = null,
)
