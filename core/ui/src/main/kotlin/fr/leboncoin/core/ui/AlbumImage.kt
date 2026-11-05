package fr.leboncoin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.adevinta.spark.SparkTheme

/**
 * Remote image with a neutral placeholder.
 *
 * @param placeholderUrl an image already in memory (e.g. the list thumbnail) displayed
 * instantly while [url] loads.
 */
@Composable
fun AlbumImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholderUrl: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val request = remember(url, placeholderUrl) {
        ImageRequest.Builder(context)
            .data(url)
            .placeholderMemoryCacheKey(placeholderUrl)
            .build()
    }
    val placeholder = ColorPainter(SparkTheme.colors.neutralContainer)
    AsyncImage(
        model = request,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        contentScale = contentScale,
    )
}
