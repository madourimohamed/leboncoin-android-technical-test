package fr.leboncoin.core.testing

import androidx.paging.PagingData
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.RefreshResult
import fr.leboncoin.core.domain.repository.AlbumRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** In-memory [AlbumRepository] whose remote behaviour is driven by the test. */
class FakeAlbumRepository : AlbumRepository {

    private val cachedAlbums = MutableStateFlow<List<Album>>(emptyList())
    private val favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    /** Albums returned by the "remote" on the next successful refresh. */
    var remoteAlbums: List<Album> = emptyList()

    /** Result of the next refreshes. */
    var refreshResult: RefreshResult = RefreshResult.Success

    /** When set, refresh suspends until it is completed: lets tests observe in-flight states. */
    var refreshGate: CompletableDeferred<Unit>? = null

    var refreshCount = 0
        private set

    private val albums: Flow<List<Album>> =
        combine(cachedAlbums, favoriteIds) { albums, favorites ->
            albums.map { it.copy(isFavorite = it.id in favorites) }
        }

    fun setCachedAlbums(albums: List<Album>) {
        cachedAlbums.value = albums
        favoriteIds.value = albums.filter { it.isFavorite }.map { it.id }.toSet()
    }

    override fun observeAlbums(favoritesOnly: Boolean): Flow<PagingData<Album>> =
        albums.map { list -> PagingData.from(if (favoritesOnly) list.filter { it.isFavorite } else list) }

    override fun observeAlbum(id: Int): Flow<Album?> =
        albums.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun refresh(): RefreshResult {
        refreshCount++
        refreshGate?.await()
        val result = refreshResult
        if (result == RefreshResult.Success) cachedAlbums.value = remoteAlbums
        return result
    }

    override suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        favoriteIds.update { if (isFavorite) it + id else it - id }
    }
}
