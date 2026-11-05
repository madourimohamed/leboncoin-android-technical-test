package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.data.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    @Before
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `albums are loaded when the ViewModel is created`() {
        val album = AlbumDto(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu")
        val fakeService = object : AlbumApiService {
            override suspend fun getAlbums(): List<AlbumDto> = listOf(album)
        }

        val viewModel = AlbumsViewModel(AlbumRepository(fakeService))

        assertEquals(listOf(album), viewModel.albums.value)
    }
}
