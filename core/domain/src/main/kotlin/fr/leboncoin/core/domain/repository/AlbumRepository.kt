package fr.leboncoin.core.domain.repository

import androidx.paging.PagingData
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.RefreshResult
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for albums.
 *
 * Reads always come from the local cache so that the app works offline;
 * [refresh] synchronizes this cache with the remote source.
 */
interface AlbumRepository {

    /** Paged albums, optionally restricted to favorites. Emits again on every local change. */
    fun observeAlbums(favoritesOnly: Boolean): Flow<PagingData<Album>>

    /** The album with the given [id], or `null` if it is not (or no longer) in the cache. */
    fun observeAlbum(id: Int): Flow<Album?>

    /** Fetches remote albums and replaces the cached ones. Favorites are preserved. */
    suspend fun refresh(): RefreshResult

    suspend fun setFavorite(id: Int, isFavorite: Boolean)
}
