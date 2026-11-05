package fr.leboncoin.core.data

import androidx.paging.testing.asSnapshot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import fr.leboncoin.core.data.repository.OfflineFirstAlbumRepository
import fr.leboncoin.core.database.AppDatabase
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.DataError
import fr.leboncoin.core.domain.model.RefreshResult
import fr.leboncoin.core.network.AlbumApi
import fr.leboncoin.core.network.model.NetworkAlbum
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class OfflineFirstAlbumRepositoryTest {

    private val api = FakeAlbumApi()
    private lateinit var database: AppDatabase
    private lateinit var repository: OfflineFirstAlbumRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        repository = OfflineFirstAlbumRepository(
            albumApi = api,
            albumDao = database.albumDao(),
            favoriteDao = database.favoriteDao(),
            defaultDispatcher = UnconfinedTestDispatcher(),
        )
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun `refresh stores remote albums in the cache`() = runTest {
        api.albums = listOf(networkAlbum(1), networkAlbum(2))

        val result = repository.refresh()

        assertEquals(RefreshResult.Success, result)
        assertEquals(listOf(album(1), album(2)), repository.observeAlbums(favoritesOnly = false).asSnapshot())
    }

    @Test
    fun `refresh failure keeps the existing cache available offline`() = runTest {
        api.albums = listOf(networkAlbum(1))
        repository.refresh()
        api.error = IOException("offline")

        val result = repository.refresh()

        assertEquals(RefreshResult.Failure(DataError.Network), result)
        assertEquals(listOf(album(1)), repository.observeAlbums(favoritesOnly = false).asSnapshot())
    }

    @Test
    fun `refresh maps failures to domain errors`() = runTest {
        val cases = mapOf(
            IOException() to DataError.Network,
            HttpException(Response.error<Any>(503, "".toResponseBody())) to DataError.Server,
            SerializationException() to DataError.Server,
            IllegalStateException() to DataError.Unknown,
        )
        cases.forEach { (exception, expected) ->
            api.error = exception
            assertEquals(RefreshResult.Failure(expected), repository.refresh())
        }
    }

    @Test
    fun `favorites survive a refresh and can be filtered`() = runTest {
        api.albums = listOf(networkAlbum(1), networkAlbum(2), networkAlbum(3))
        repository.refresh()
        repository.setFavorite(id = 2, isFavorite = true)

        repository.refresh()

        assertEquals(
            listOf(album(2, isFavorite = true)),
            repository.observeAlbums(favoritesOnly = true).asSnapshot(),
        )
    }

    @Test
    fun `observeAlbum reflects favorite changes`() = runTest {
        api.albums = listOf(networkAlbum(1))
        repository.refresh()

        repository.setFavorite(id = 1, isFavorite = true)
        assertEquals(album(1, isFavorite = true), repository.observeAlbum(1).first())

        repository.setFavorite(id = 1, isFavorite = false)
        assertEquals(album(1, isFavorite = false), repository.observeAlbum(1).first())
    }

    @Test
    fun `observeAlbum emits null for an unknown album`() = runTest {
        assertNull(repository.observeAlbum(42).first())
    }

    private class FakeAlbumApi : AlbumApi {
        var albums: List<NetworkAlbum> = emptyList()
        var error: Exception? = null

        override suspend fun getAlbums(): List<NetworkAlbum> = error?.let { throw it } ?: albums
    }

    private fun networkAlbum(id: Int) = NetworkAlbum(
        id = id,
        albumId = 1,
        title = "title $id",
        url = "url $id",
        thumbnailUrl = "thumbnail $id",
    )

    private fun album(id: Int, isFavorite: Boolean = false) = Album(
        id = id,
        albumId = 1,
        title = "title $id",
        imageUrl = "url $id",
        thumbnailUrl = "thumbnail $id",
        isFavorite = isFavorite,
    )
}
