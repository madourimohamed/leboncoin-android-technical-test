package fr.leboncoin.feature.albums.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import fr.leboncoin.feature.albums.AlbumsScreen
import kotlinx.serialization.Serializable

@Serializable
data object AlbumsRoute

fun NavGraphBuilder.albumsScreen(onAlbumClick: (albumId: Int) -> Unit) {
    composable<AlbumsRoute> {
        AlbumsScreen(onAlbumClick = onAlbumClick)
    }
}
