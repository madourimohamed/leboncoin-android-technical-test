package fr.leboncoin.feature.albums

import androidx.lifecycle.SavedStateHandle
import androidx.paging.testing.asSnapshot
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.domain.model.DataError
import fr.leboncoin.core.domain.model.RefreshResult
import fr.leboncoin.core.testing.FakeAlbumRepository
import fr.leboncoin.core.testing.MainDispatcherRule
import fr.leboncoin.core.testing.TestAnalyticsHelper
import fr.leboncoin.core.testing.testAlbum
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AlbumsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeAlbumRepository()
    private val analyticsHelper = TestAnalyticsHelper()
    private val savedStateHandle = SavedStateHandle()

    private fun createViewModel() = AlbumsViewModel(repository, analyticsHelper, savedStateHandle)

    @Test
    fun `refreshes and tracks the screen view once on creation`() {
        createViewModel()

        assertEquals(1, repository.refreshCount)
        assertEquals(
            listOf(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.SCREEN_VIEW,
                    params = mapOf(AnalyticsEvent.Params.SCREEN_NAME to "albums"),
                ),
            ),
            analyticsHelper.events,
        )
    }

    @Test
    fun `isRefreshing is true while a refresh is in progress`() {
        val gate = CompletableDeferred<Unit>()
        repository.refreshGate = gate

        val viewModel = createViewModel()
        assertTrue(viewModel.uiState.value.isRefreshing)

        gate.complete(Unit)
        assertFalse(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun `refresh is ignored while another one is in progress`() {
        repository.refreshGate = CompletableDeferred()
        val viewModel = createViewModel()

        viewModel.refresh()
        viewModel.refresh()

        assertEquals(1, repository.refreshCount)
    }

    @Test
    fun `refresh failure is exposed then cleared once shown`() {
        repository.refreshResult = RefreshResult.Failure(DataError.Network)

        val viewModel = createViewModel()
        assertEquals(DataError.Network, viewModel.uiState.value.error)

        viewModel.onErrorShown()
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `retry clears the previous error on success`() {
        repository.refreshResult = RefreshResult.Failure(DataError.Server)
        val viewModel = createViewModel()

        repository.refreshResult = RefreshResult.Success
        viewModel.refresh()

        assertNull(viewModel.uiState.value.error)
        assertEquals(2, repository.refreshCount)
    }

    @Test
    fun `albums exposes the refreshed albums`() = runTest {
        repository.remoteAlbums = listOf(testAlbum(1), testAlbum(2))

        val viewModel = createViewModel()

        assertEquals(listOf(testAlbum(1), testAlbum(2)), viewModel.albums.asSnapshot())
    }

    @Test
    fun `favorites filter restricts albums and is saved for process death`() = runTest {
        repository.remoteAlbums = listOf(testAlbum(1), testAlbum(2, isFavorite = true))
        repository.setCachedAlbums(repository.remoteAlbums)
        val viewModel = createViewModel()

        viewModel.onFavoritesOnlyChange(true)

        assertTrue(viewModel.uiState.value.favoritesOnly)
        assertEquals(true, savedStateHandle.get<Boolean>(AlbumsViewModel.FAVORITES_ONLY_KEY))
        assertEquals(listOf(2), viewModel.albums.asSnapshot().map { it.id })
    }

    @Test
    fun `favorites filter is restored from saved state`() {
        savedStateHandle[AlbumsViewModel.FAVORITES_ONLY_KEY] = true

        val viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.favoritesOnly)
    }

    @Test
    fun `onFavoriteClick toggles the favorite and tracks it`() = runTest {
        repository.remoteAlbums = listOf(testAlbum(1))
        val viewModel = createViewModel()

        viewModel.onFavoriteClick(testAlbum(1, isFavorite = false))

        assertEquals(listOf(testAlbum(1, isFavorite = true)), viewModel.albums.asSnapshot())
        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.FAVORITE_TOGGLED,
                    params = mapOf(
                        AnalyticsEvent.Params.ALBUM_ID to "1",
                        AnalyticsEvent.Params.IS_FAVORITE to "true",
                    ),
                ),
            ),
        )
    }

    @Test
    fun `onAlbumClick tracks the selection`() {
        val viewModel = createViewModel()

        viewModel.onAlbumClick(testAlbum(7))

        assertTrue(
            analyticsHelper.hasLogged(
                AnalyticsEvent(
                    type = AnalyticsEvent.Types.ALBUM_SELECTED,
                    params = mapOf(AnalyticsEvent.Params.ALBUM_ID to "7"),
                ),
            ),
        )
    }
}
