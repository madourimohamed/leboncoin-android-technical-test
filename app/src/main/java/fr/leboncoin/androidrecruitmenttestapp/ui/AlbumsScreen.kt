package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.androidrecruitmenttestapp.AlbumsViewModel
import fr.leboncoin.data.network.model.AlbumDto

@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onItemSelected : (AlbumDto) -> Unit,
    modifier: Modifier = Modifier,
) {
    val albums by viewModel.albums.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = it,
        ) {
            items(
                items = albums,
                key = { album -> album.id }
            ) { album ->
                AlbumItem(
                    album = album,
                    onItemSelected = onItemSelected,
                )
            }
        }
    }
}