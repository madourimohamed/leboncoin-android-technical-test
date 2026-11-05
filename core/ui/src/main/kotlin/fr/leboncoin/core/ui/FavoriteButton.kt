package fr.leboncoin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.adevinta.spark.components.iconbuttons.IconButtonGhost
import com.adevinta.spark.components.iconbuttons.IconButtonIntent
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.LikeOutline
import com.adevinta.spark.icons.SparkIcons

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButtonGhost(
        icon = if (isFavorite) SparkIcons.LikeFill else SparkIcons.LikeOutline,
        onClick = onClick,
        modifier = modifier,
        intent = if (isFavorite) IconButtonIntent.Danger else IconButtonIntent.Neutral,
        contentDescription = stringResource(
            if (isFavorite) R.string.core_ui_remove_favorite else R.string.core_ui_add_favorite,
        ),
    )
}
