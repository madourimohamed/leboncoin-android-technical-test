package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.leboncoin.feature.albums.navigation.AlbumsRoute
import fr.leboncoin.feature.albums.navigation.albumsScreen
import fr.leboncoin.feature.details.navigation.albumDetailScreen
import fr.leboncoin.feature.details.navigation.navigateToAlbumDetail

/**
 * Wires the features together. Features never reference each other: they expose
 * callbacks, and the app decides where each one leads.
 */
@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AlbumsRoute,
        modifier = modifier,
    ) {
        albumsScreen(onAlbumClick = navController::navigateToAlbumDetail)
        albumDetailScreen(onBackClick = navController::navigateUp)
    }
}
