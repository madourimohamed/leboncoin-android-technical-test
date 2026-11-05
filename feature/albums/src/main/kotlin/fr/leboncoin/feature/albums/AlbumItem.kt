package fr.leboncoin.feature.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.tags.TagTinted
import com.adevinta.spark.components.text.Text
import fr.leboncoin.core.domain.model.Album
import fr.leboncoin.core.ui.AlbumImage
import fr.leboncoin.core.ui.FavoriteButton

@OptIn(ExperimentalSparkApi::class)
@Composable
internal fun AlbumItem(
    album: Album,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp),
        onClick = onClick,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AlbumImage(
                url = album.thumbnailUrl,
                // The title is read right after: describing the image would be redundant.
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
            ) {
                Text(
                    text = album.title,
                    style = SparkTheme.typography.body2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.weight(1f))
                // Tags, not chips: chips are interactive and would steal the card clicks.
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TagTinted(text = stringResource(R.string.feature_albums_album_number, album.albumId))
                    TagTinted(text = stringResource(R.string.feature_albums_track_number, album.id))
                }
            }
            FavoriteButton(
                isFavorite = album.isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier.padding(end = 4.dp),
            )
        }
    }
}

@Preview
@Composable
private fun AlbumItemPreview() {
    SparkTheme {
        AlbumItem(
            album = Album(
                id = 1,
                albumId = 1,
                title = "accusamus beatae ad facilis cum similique qui sunt",
                imageUrl = "",
                thumbnailUrl = "",
                isFavorite = true,
            ),
            onClick = {},
            onFavoriteClick = {},
        )
    }
}
