package fr.leboncoin.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.leboncoin.core.database.model.AlbumEntity
import fr.leboncoin.core.database.model.PopulatedAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query(
        """
        SELECT albums.*, EXISTS(SELECT 1 FROM favorites WHERE favorites.item_id = albums.id) AS is_favorite
        FROM albums
        ORDER BY albums.id
        """,
    )
    fun pagingSource(): PagingSource<Int, PopulatedAlbum>

    @Query(
        """
        SELECT albums.*, 1 AS is_favorite
        FROM albums
        INNER JOIN favorites ON favorites.item_id = albums.id
        ORDER BY albums.id
        """,
    )
    fun favoritesPagingSource(): PagingSource<Int, PopulatedAlbum>

    @Query(
        """
        SELECT albums.*, EXISTS(SELECT 1 FROM favorites WHERE favorites.item_id = albums.id) AS is_favorite
        FROM albums
        WHERE albums.id = :id
        """,
    )
    fun observe(id: Int): Flow<PopulatedAlbum?>

    /**
     * Atomically replaces the cache: observers are notified once, with the new content,
     * and never see an intermediate empty state.
     */
    @Transaction
    suspend fun replaceAll(albums: List<AlbumEntity>) {
        deleteAll()
        insertAll(albums)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("DELETE FROM albums")
    suspend fun deleteAll()
}
