package fr.leboncoin.feature.albums

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.compose.collectAsLazyPagingItems
import com.adevinta.spark.SparkTheme
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.DataError
import fr.leboncoin.core.testing.fullyLoadedPagingData
import fr.leboncoin.core.testing.testAlbum
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "en")
class AlbumsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysAlbumsAndForwardsClicks() {
        val clicked = mutableListOf<Album>()
        val favoriteClicked = mutableListOf<Album>()
        setContent(
            albums = listOf(testAlbum(1)),
            onAlbumClick = { clicked += it },
            onFavoriteClick = { favoriteClicked += it },
        )

        composeRule.onNodeWithText("Album title 1").assertIsDisplayed().performClick()
        composeRule.onNodeWithContentDescription("Add to favorites").performClick()

        assertEquals(listOf(testAlbum(1)), clicked)
        assertEquals(listOf(testAlbum(1)), favoriteClicked)
    }

    @Test
    fun displaysFullScreenErrorWithRetryWhenNothingIsCached() {
        var retried = false
        setContent(
            albums = emptyList(),
            uiState = AlbumsUiState(error = DataError.Network),
            onRefresh = { retried = true },
        )

        composeRule
            .onNodeWithText("No internet connection. Check your network and try again.")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Retry").performClick()

        assertTrue(retried)
    }

    @Test
    fun displaysEmptyFavoritesMessage() {
        setContent(albums = emptyList(), uiState = AlbumsUiState(favoritesOnly = true))

        composeRule.onNodeWithText("No favorites yet.", substring = true).assertIsDisplayed()
    }

    private fun setContent(
        albums: List<Album>,
        uiState: AlbumsUiState = AlbumsUiState(),
        onAlbumClick: (Album) -> Unit = {},
        onFavoriteClick: (Album) -> Unit = {},
        onRefresh: () -> Unit = {},
    ) {
        val pagingData = flowOf(fullyLoadedPagingData(albums))
        composeRule.setContent {
            SparkTheme {
                AlbumsScreen(
                    uiState = uiState,
                    albums = pagingData.collectAsLazyPagingItems(),
                    onAlbumClick = onAlbumClick,
                    onFavoriteClick = onFavoriteClick,
                    onFavoritesOnlyChange = {},
                    onRefresh = onRefresh,
                    onErrorShown = {},
                )
            }
        }
    }
}
