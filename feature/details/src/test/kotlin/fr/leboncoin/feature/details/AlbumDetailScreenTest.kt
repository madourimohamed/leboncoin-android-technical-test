package fr.leboncoin.feature.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adevinta.spark.SparkTheme
import fr.leboncoin.core.testing.testAlbum
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "en")
class AlbumDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysAlbumAndForwardsFavoriteClick() {
        var favoriteClicked = false
        composeRule.setContent {
            SparkTheme {
                AlbumDetailScreen(
                    uiState = AlbumDetailUiState.Success(testAlbum(3, isFavorite = true)),
                    onBackClick = {},
                    onFavoriteClick = { favoriteClicked = true },
                )
            }
        }

        composeRule.onNodeWithText("Album title 3").assertIsDisplayed()
        composeRule.onNodeWithText("Track #3").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Remove from favorites").performClick()

        assertTrue(favoriteClicked)
    }

    @Test
    fun displaysNotFoundMessage() {
        composeRule.setContent {
            SparkTheme {
                AlbumDetailScreen(
                    uiState = AlbumDetailUiState.NotFound,
                    onBackClick = {},
                    onFavoriteClick = {},
                )
            }
        }

        composeRule.onNodeWithText("This album is no longer available.").assertIsDisplayed()
    }
}
