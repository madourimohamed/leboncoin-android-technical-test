package fr.leboncoin.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import fr.leboncoin.feature.details.AlbumDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDetailRoute(val albumId: Int)

fun NavController.navigateToAlbumDetail(albumId: Int) {
    // Single top: a fast double tap must not stack the same screen twice.
    navigate(AlbumDetailRoute(albumId)) { launchSingleTop = true }
}

fun NavGraphBuilder.albumDetailScreen(onBackClick: () -> Unit) {
    composable<AlbumDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AlbumDetailRoute>()
        AlbumDetailScreen(albumId = route.albumId, onBackClick = onBackClick)
    }
}
