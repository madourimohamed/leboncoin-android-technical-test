package fr.leboncoin.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import fr.leboncoin.core.common.di.AppDispatchers
import fr.leboncoin.core.common.di.Dispatcher
import fr.leboncoin.core.data.model.asEntity
import fr.leboncoin.core.data.model.asExternalModel
import fr.leboncoin.core.database.dao.AlbumDao
import fr.leboncoin.core.database.dao.FavoriteDao
import fr.leboncoin.core.database.model.FavoriteEntity
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.DataError
import fr.leboncoin.core.domain.model.RefreshResult
import fr.leboncoin.core.domain.repository.AlbumRepository
import fr.leboncoin.core.network.AlbumApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Offline-first implementation: the database is the single source of truth exposed to the UI,
 * the network is only used to update it.
 */
internal class OfflineFirstAlbumRepository @Inject constructor(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val favoriteDao: FavoriteDao,
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
) : AlbumRepository {

    override fun observeAlbums(favoritesOnly: Boolean): Flow<PagingData<Album>> = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = {
            if (favoritesOnly) albumDao.favoritesPagingSource() else albumDao.pagingSource()
        },
    ).flow.map { pagingData -> pagingData.map { it.asExternalModel() } }

    override fun observeAlbum(id: Int): Flow<Album?> = albumDao.observe(id)
        .map { it?.asExternalModel() }
        .distinctUntilChanged()

    override suspend fun refresh(): RefreshResult = try {
        val albums = albumApi.getAlbums()
        // ~5000 entries: map off the main thread.
        val entities = withContext(defaultDispatcher) { albums.map { it.asEntity() } }
        albumDao.replaceAll(entities)
        RefreshResult.Success
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        RefreshResult.Failure(e.toDataError())
    }

    override suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            favoriteDao.insert(FavoriteEntity(itemId = id))
        } else {
            favoriteDao.delete(itemId = id)
        }
    }

    private fun Exception.toDataError(): DataError = when (this) {
        is IOException -> DataError.Network
        is HttpException, is SerializationException -> DataError.Server
        else -> DataError.Unknown
    }

    private companion object {
        const val PAGE_SIZE = 50
    }
}
