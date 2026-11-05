package fr.leboncoin.core.analytics

/**
 * Entry point for analytics. Features depend on this interface only, so that the
 * actual SDK (Firebase, Amplitude, in-house...) can be swapped from a single module.
 */
interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}

fun AnalyticsHelper.logScreenView(screenName: String) = logEvent(
    AnalyticsEvent(
        type = AnalyticsEvent.Types.SCREEN_VIEW,
        params = mapOf(AnalyticsEvent.Params.SCREEN_NAME to screenName),
    ),
)

fun AnalyticsHelper.logAlbumSelected(albumId: Int) = logEvent(
    AnalyticsEvent(
        type = AnalyticsEvent.Types.ALBUM_SELECTED,
        params = mapOf(AnalyticsEvent.Params.ALBUM_ID to albumId.toString()),
    ),
)

fun AnalyticsHelper.logFavoriteToggled(albumId: Int, isFavorite: Boolean) = logEvent(
    AnalyticsEvent(
        type = AnalyticsEvent.Types.FAVORITE_TOGGLED,
        params = mapOf(
            AnalyticsEvent.Params.ALBUM_ID to albumId.toString(),
            AnalyticsEvent.Params.IS_FAVORITE to isFavorite.toString(),
        ),
    ),
)
