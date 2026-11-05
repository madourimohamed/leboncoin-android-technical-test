package fr.leboncoin.feature.details

import app.cash.turbine.test
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.testing.FakeAlbumRepository
import fr.leboncoin.core.testing.MainDispatcherRule
import fr.leboncoin.core.testing.TestAnalyticsHelper
import fr.leboncoin.core.testing.testAlbum
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AlbumDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAlbumRepository()
    private val analyticsHelper = TestAnalyticsHelper()

    private fun createViewModel(albumId: Int) =
        AlbumDetailViewModel(albumId, repository, analyticsHelper)

    @Test
    fun `initial state is loading`() {
        assertEquals(AlbumDetailUiState.Loading, createViewModel(1).uiState.value)
    }

    @Test
    fun `emits the requested album`() = runTest {
        repository.setCachedAlbums(listOf(testAlbum(1), testAlbum(2)))

        createViewModel(2).uiState.test {
            assertEquals(AlbumDetailUiState.Success(testAlbum(2)), awaitItem())
        }
    }

    @Test
    fun `emits not found for an unknown album`() = runTest {
        repository.setCachedAlbums(listOf(testAlbum(1)))

        createViewModel(42).uiState.test {
            assertEquals(AlbumDetailUiState.NotFound, awaitItem())
        }
    }

    @Test
    fun `onFavoriteClick toggles the favorite`() = runTest {
        repository.setCachedAlbums(listOf(testAlbum(1)))
        val viewModel = createViewModel(1)

        viewModel.uiState.test {
            assertEquals(AlbumDetailUiState.Success(testAlbum(1, isFavorite = false)), awaitItem())

            viewModel.onFavoriteClick()
            assertEquals(AlbumDetailUiState.Success(testAlbum(1, isFavorite = true)), awaitItem())

            viewModel.onFavoriteClick()
            assertEquals(AlbumDetailUiState.Success(testAlbum(1, isFavorite = false)), awaitItem())
        }
    }

    @Test
    fun `onFavoriteClick is ignored until the album is loaded`() {
        val viewModel = createViewModel(1)

        viewModel.onFavoriteClick()

        assertTrue(analyticsHelper.events.none { it.type == AnalyticsEvent.Types.FAVORITE_TOGGLED })
    }

    @Test
    fun `tracks the screen view on creation`() {
        createViewModel(1)

        assertEquals(
            listOf(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.SCREEN_VIEW,
                    params = mapOf(AnalyticsEvent.Params.SCREEN_NAME to "album_detail"),
                ),
            ),
            analyticsHelper.events,
        )
    }
}
