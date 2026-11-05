package fr.leboncoin.core.analytics

import android.util.Log
import javax.inject.Inject

/** Stand-in implementation that logs events; holds no Context, hence cannot leak one. */
internal class LogcatAnalyticsHelper @Inject constructor() : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        Log.d(TAG, "${event.type} ${event.params}")
    }

    private companion object {
        const val TAG = "Analytics"
    }
}
