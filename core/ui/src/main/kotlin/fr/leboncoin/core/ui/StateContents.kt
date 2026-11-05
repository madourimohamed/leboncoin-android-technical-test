package fr.leboncoin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.buttons.ButtonFilled
import com.adevinta.spark.components.icons.Icon
import com.adevinta.spark.components.icons.IconSize
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.icons.SparkIcon

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Spinner()
    }
}

/** Full-screen message with an optional action, used for empty and error states. */
@Composable
fun MessageContent(
    icon: SparkIcon,
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            sparkIcon = icon,
            contentDescription = null,
            size = IconSize.ExtraLarge,
            tint = SparkTheme.colors.onSurface,
        )
        Text(
            text = message,
            style = SparkTheme.typography.body1,
            textAlign = TextAlign.Center,
        )
        if (onRetry != null) {
            ButtonFilled(onClick = onRetry) {
                Text(text = stringResource(R.string.core_ui_retry))
            }
        }
    }
}
