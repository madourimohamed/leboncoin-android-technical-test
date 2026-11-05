package fr.leboncoin.feature.albums

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import com.adevinta.spark.components.snackbars.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.appbar.TopAppBar
import com.adevinta.spark.components.chips.ChipSelectable
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.LikeOutline
import com.adevinta.spark.icons.PhotoPack
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WarningOutline
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.domain.model.DataError
import fr.leboncoin.core.ui.LoadingContent
import fr.leboncoin.core.ui.MessageContent

@Composable
internal fun AlbumsScreen(
    onAlbumClick: (albumId: Int) -> Unit,
    viewModel: AlbumsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val albums = viewModel.albums.collectAsLazyPagingItems()

    AlbumsScreen(
        uiState = uiState,
        albums = albums,
        onAlbumClick = { album ->
            viewModel.onAlbumClick(album)
            onAlbumClick(album.id)
        },
        onFavoriteClick = viewModel::onFavoriteClick,
        onFavoritesOnlyChange = viewModel::onFavoritesOnlyChange,
        onRefresh = viewModel::refresh,
        onErrorShown = viewModel::onErrorShown,
    )
}

@OptIn(ExperimentalSparkApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumsScreen(
    uiState: AlbumsUiState,
    albums: LazyPagingItems<Album>,
    onAlbumClick: (Album) -> Unit,
    onFavoriteClick: (Album) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val hasContent = albums.itemCount > 0

    // With content on screen, a failed refresh is a transient message: the cache is still shown.
    // Without content, the error is displayed full screen instead (see below).
    val error = uiState.error
    if (error != null && hasContent) {
        val message = stringResource(error.messageRes)
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message)
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feature_albums_title)) },
                actions = {
                    ChipSelectable(
                        selected = uiState.favoritesOnly,
                        onClick = { onFavoritesOnlyChange(!uiState.favoritesOnly) },
                        content = { Text(stringResource(R.string.feature_albums_favorites_filter)) },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                hasContent -> AlbumList(
                    albums = albums,
                    onAlbumClick = onAlbumClick,
                    onFavoriteClick = onFavoriteClick,
                )

                albums.loadState.refresh is LoadState.Loading || uiState.isRefreshing -> LoadingContent()

                uiState.favoritesOnly -> MessageContent(
                    icon = SparkIcons.LikeOutline,
                    message = stringResource(R.string.feature_albums_no_favorites),
                )

                error != null -> MessageContent(
                    icon = SparkIcons.WarningOutline,
                    message = stringResource(error.messageRes),
                    onRetry = onRefresh,
                )

                else -> MessageContent(
                    icon = SparkIcons.PhotoPack,
                    message = stringResource(R.string.feature_albums_empty),
                    onRetry = onRefresh,
                )
            }
        }
    }
}

@Composable
private fun AlbumList(
    albums: LazyPagingItems<Album>,
    onAlbumClick: (Album) -> Unit,
    onFavoriteClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = albums.itemCount,
            // Stable keys keep the scroll position and item state when the data changes.
            key = albums.itemKey { it.id },
            contentType = albums.itemContentType { "album" },
        ) { index ->
            val album = albums[index] ?: return@items
            AlbumItem(
                album = album,
                onClick = { onAlbumClick(album) },
                onFavoriteClick = { onFavoriteClick(album) },
            )
        }
    }
}

@get:StringRes
private val DataError.messageRes: Int
    get() = when (this) {
        DataError.Network -> R.string.feature_albums_error_network
        DataError.Server -> R.string.feature_albums_error_server
        DataError.Unknown -> R.string.feature_albums_error_unknown
    }
