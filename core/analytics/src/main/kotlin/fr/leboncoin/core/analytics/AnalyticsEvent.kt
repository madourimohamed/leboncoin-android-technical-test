package fr.leboncoin.core.analytics

/** A vendor-agnostic analytics event. */
data class AnalyticsEvent(
    val type: String,
    val params: Map<String, String> = emptyMap(),
) {
    object Types {
        const val SCREEN_VIEW = "screen_view"
        const val ALBUM_SELECTED = "album_selected"
        const val FAVORITE_TOGGLED = "favorite_toggled"
    }

    object Params {
        const val SCREEN_NAME = "screen_name"
        const val ALBUM_ID = "album_id"
        const val IS_FAVORITE = "is_favorite"
    }
}
