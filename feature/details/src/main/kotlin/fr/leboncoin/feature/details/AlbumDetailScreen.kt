package fr.leboncoin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.appbar.TopAppBar
import com.adevinta.spark.components.navigation.UpNavigationIcon
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.WarningOutline
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.ui.AlbumImage
import fr.leboncoin.core.ui.FavoriteButton
import fr.leboncoin.core.ui.LoadingContent
import fr.leboncoin.core.ui.MessageContent

@Composable
internal fun AlbumDetailScreen(
    albumId: Int,
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel =
        hiltViewModel<AlbumDetailViewModel, AlbumDetailViewModel.Factory> { factory ->
            factory.create(albumId)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::onFavoriteClick,
    )
}

@OptIn(ExperimentalSparkApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumDetailScreen(
    uiState: AlbumDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feature_details_title)) },
                navigationIcon = { UpNavigationIcon(onClick = onBackClick) },
                actions = {
                    if (uiState is AlbumDetailUiState.Success) {
                        FavoriteButton(
                            isFavorite = uiState.album.isFavorite,
                            onClick = onFavoriteClick,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        when (uiState) {
            AlbumDetailUiState.Loading -> LoadingContent(contentModifier)
            AlbumDetailUiState.NotFound -> MessageContent(
                icon = SparkIcons.WarningOutline,
                message = stringResource(R.string.feature_details_not_found),
                modifier = contentModifier,
            )

            is AlbumDetailUiState.Success -> AlbumDetailContent(uiState.album, contentModifier)
        }
    }
}

@Composable
private fun AlbumDetailContent(album: Album, modifier: Modifier = Modifier) {
    // Scrollable and width-capped: usable in landscape and on large screens.
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 560.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AlbumImage(
                url = album.imageUrl,
                contentDescription = album.title,
                // The thumbnail is already in memory: shown instantly while the full image loads.
                placeholderUrl = album.thumbnailUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
            Text(text = album.title, style = SparkTheme.typography.headline1)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TagTinted(text = stringResource(R.string.feature_details_album_number, album.albumId))
                TagTinted(text = stringResource(R.string.feature_details_track_number, album.id))
            }
        }
    }
}

@Preview
@Composable
private fun AlbumDetailScreenPreview() {
    SparkTheme {
        AlbumDetailScreen(
            uiState = AlbumDetailUiState.Success(
                Album(
                    id = 1,
                    albumId = 1,
                    title = "accusamus beatae ad facilis cum similique qui sunt",
                    imageUrl = "",
                    thumbnailUrl = "",
                    isFavorite = false,
                ),
            ),
            onBackClick = {},
            onFavoriteClick = {},
        )
    }
}
