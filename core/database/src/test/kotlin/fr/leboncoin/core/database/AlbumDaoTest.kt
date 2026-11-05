package fr.leboncoin.core.database

import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadResult
import androidx.paging.testing.TestPager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import fr.leboncoin.core.database.dao.AlbumDao
import fr.leboncoin.core.database.dao.FavoriteDao
import fr.leboncoin.core.database.model.AlbumEntity
import fr.leboncoin.core.database.model.FavoriteEntity
import fr.leboncoin.core.database.model.PopulatedAlbum
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AlbumDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var albumDao: AlbumDao
    private lateinit var favoriteDao: FavoriteDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        albumDao = database.albumDao()
        favoriteDao = database.favoriteDao()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun `pagingSource returns albums ordered by id with their favorite status`() = runTest {
        albumDao.insertAll(listOf(album(3), album(1), album(2)))
        favoriteDao.insert(FavoriteEntity(itemId = 2))

        val page = albumDao.pagingSource().loadAll()

        assertEquals(
            listOf(1 to false, 2 to true, 3 to false),
            page.map { it.album.id to it.isFavorite },
        )
    }

    @Test
    fun `favoritesPagingSource only returns favorite albums`() = runTest {
        albumDao.insertAll(listOf(album(1), album(2), album(3)))
        favoriteDao.insert(FavoriteEntity(itemId = 3))
        favoriteDao.insert(FavoriteEntity(itemId = 1))

        val page = albumDao.favoritesPagingSource().loadAll()

        assertEquals(listOf(1, 3), page.map { it.album.id })
    }

    @Test
    fun `replaceAll replaces the cache but keeps favorites`() = runTest {
        albumDao.insertAll(listOf(album(1), album(2)))
        favoriteDao.insert(FavoriteEntity(itemId = 2))

        albumDao.replaceAll(listOf(album(2, title = "updated"), album(3)))

        val page = albumDao.pagingSource().loadAll()
        assertEquals(
            listOf(PopulatedAlbum(album(2, title = "updated"), true), PopulatedAlbum(album(3), false)),
            page,
        )
    }

    @Test
    fun `observe emits the album and reflects favorite changes`() = runTest {
        albumDao.insertAll(listOf(album(1)))

        assertEquals(false, albumDao.observe(1).first()?.isFavorite)
        favoriteDao.insert(FavoriteEntity(itemId = 1))
        assertEquals(true, albumDao.observe(1).first()?.isFavorite)
        favoriteDao.delete(itemId = 1)
        assertEquals(false, albumDao.observe(1).first()?.isFavorite)
    }

    @Test
    fun `observe emits null for an unknown album`() = runTest {
        assertNull(albumDao.observe(42).first())
    }

    private suspend fun androidx.paging.PagingSource<Int, PopulatedAlbum>.loadAll(): List<PopulatedAlbum> {
        val result = TestPager(PagingConfig(pageSize = 50), this).refresh()
        return (result as LoadResult.Page).data
    }

    private fun album(id: Int, title: String = "title $id") = AlbumEntity(
        id = id,
        albumId = 1,
        title = title,
        url = "https://placehold.co/600x600/$id",
        thumbnailUrl = "https://placehold.co/150x150/$id",
    )
}
